package de.phyrone.voicechatapp.server.eventbus

import de.phyrone.voicechatapp.server.api.EventBus
import de.phyrone.voicechatapp.server.api.Subscribe
import de.phyrone.voicechatapp.server.api.logger
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

/*
 * TODO add support for functions without classes
 * TODO add support for canceling events
 * TODO maybe sticky events but i dont think its a good idea
 * TODO allow listeners to be registered to the eventflow (invoke truly async and independent (no canceling,ordering etc.))
 */
class DefaultEventBus : EventBus {

  private val eventFlow = MutableSharedFlow<Any>(0, Int.MAX_VALUE)

  // Snapshots allow to posts events while modifying the listeners without blocking or synchronizing
  // this comes with a performance penalty on modifying the listeners
  // and can use more memory but gives much less headaches about concurrency
  // update implementation is provided by kotlinx.coroutines and is thread safe (optimistic locking
  // if im not mistaken)
  private val invokerSnapshot = MutableStateFlow(ListenersSnapshot())
  override suspend fun post(event: Any, gracefulErrorHandling: Boolean) {
    coroutineScope {
      val asyncTasks =
          mutableListOf(
              launch {
                eventFlow.emit(
                    event,
                ) // does not block but unblocks the awaiting functions... unless you send
                // 2^31-1 events at once
              },
          )

      invokerSnapshot
          .first()
          .findInvokers(event)
          .sortedBy { it.priority }
          .forEach { eventInvoker: EventInvoker ->
            if (eventInvoker.async) {
              asyncTasks.add(
                  launch { invokeHandler(eventInvoker, event, gracefulErrorHandling) },
              )
            } else {
              invokeHandler(eventInvoker, event, gracefulErrorHandling)
            }
          } // TODO error handling

      asyncTasks.joinAll()
    }
  }

  private suspend fun invokeHandler(
      invoker: EventInvoker,
      event: Any,
      gracefulErrorHandling: Boolean,
  ) {
    if (gracefulErrorHandling) {
      try {
        invoker.invoke(event)
      } catch (e: Exception) {
        LOGGER.atWarning().withCause(e).log("Failed to invoke event %s for %s", event, invoker)
      }
    } else {
      invoker.invoke(event)
    }
  }

  @Suppress("UNCHECKED_CAST")
  override suspend fun <T : Any> await(event: KClass<T>): T =
      eventFlow.first { event.isInstance(it) } as T

  override fun register(listener: Any) {
    val invokers =
        listener::class
            .functions
            // TODO filter out private functions
            .mapNotNull { it.findAnnotation<Subscribe>()?.let { annotation -> it to annotation } }
            .mapNotNull { (function, annotation) ->
              runCatching {
                    kFunToInvoker(
                        listener,
                        function,
                        annotation,
                    )
                  }
                  .getOrElse { error ->
                    LOGGER.atWarning()
                        .withCause(error)
                        .log("Failed to register '%s' for listener %s", function, listener)
                    null
                  }
            }
    invokerSnapshot.update { it.withListener(listener, invokers) }
  }

  override fun unregister(listener: Any) {
    invokerSnapshot.update { it.withoutListener(listener) }
  }

  override fun isRegistered(listener: Any): Boolean {
    return invokerSnapshot.value.listeners.containsKey(listener)
  }

  override fun unregisterAll() {
    invokerSnapshot.update { ListenersSnapshot() }
  }

  override fun getRegisteredListeners(): List<Any> {
    return invokerSnapshot.value.listeners.keys.toList()
  }

  private data class ListenersSnapshot(
      val invokers: Iterable<EventInvoker> = emptyList(),
      val listeners: Map<Any, Iterable<EventInvoker>> = emptyMap(),
  ) {

    // incremental instead of full rebuild on every change (improves performance massively)
    private val classToInvokers: Map<KClass<*>, List<EventInvoker>> =
        invokers.groupBy { it.eventClass }

    fun findInvokers(event: Any): List<EventInvoker> {
      val types = (event::class.allSuperclasses + event::class)
      return types.mapNotNull { classToInvokers[it] }.flatten().distinct()
    }

    fun withInvoker(invoker: EventInvoker): ListenersSnapshot =
        copy(
            invokers = invokers + invoker,
        )

    fun withListener(listener: Any, invokers: Iterable<EventInvoker>): ListenersSnapshot =
        copy(
            invokers = this.invokers + invokers,
            listeners = listeners + (listener to invokers),
        )

    fun withoutListener(listener: Any): ListenersSnapshot =
        copy(
            invokers = invokers - (listeners[listener] ?: emptySet()).toSet(),
            listeners = listeners - listener,
        )

    fun withoutInvoker(invoker: EventInvoker): ListenersSnapshot =
        copy(
            invokers = invokers - invoker,
        )
  }

  private sealed class EventInvoker(
      val eventClass: KClass<*>,
      val priority: Int,
      // async is only partly correct as in its current implementation sync invokers with higher
      // priority will be executed first
      val async: Boolean,
  ) {
    abstract suspend fun invoke(event: Any)
  }

  private class SuspendClassInvoker(
      eventClass: KClass<*>,
      val listener: Any?,
      val function: KFunction<*>,
      val eventParam: KParameter,
      val instanceParam: KParameter?,
      priority: Int,
      async: Boolean,
  ) : EventInvoker(eventClass, priority, async) {
    override suspend fun invoke(event: Any) {
      val params =
          mutableMapOf(
              eventParam to event,
          )
      if (instanceParam != null && listener != null) {
        params[instanceParam] = listener
      }
      function.callSuspendBy(params)
    }
  }

  companion object {

    private val LOGGER = logger()
    private fun kFunToInvoker(
        listener: Any?,
        function: KFunction<*>,
        annotation: Subscribe,
    ): EventInvoker {
      val eventParam: KParameter
      val eventClass: KClass<*>
      val instanceParam: KParameter?
      if (listener == null) {
        require(function.parameters.size == 1) {
          "Function must have 1 parameter (instance counts as parameter too)"
        }
        eventParam = function.parameters.first()
        eventClass =
            eventParam.type.classifier as? KClass<*>
                ?: throw IllegalArgumentException("Event must be a class")
        instanceParam = null
      } else {
        require(function.parameters.size == 2) {
          "Function must have 2 parameters (instance counts as parameter too)"
        }
        require(function.parameters[0].kind == KParameter.Kind.INSTANCE) {
          "First parameter must be the instance"
        }
        require(
            function.parameters[1].kind.let {
              it == KParameter.Kind.VALUE || it == KParameter.Kind.EXTENSION_RECEIVER
            },
        ) {
          "Second parameter must be the event"
        }
        eventParam = function.parameters[1]
        instanceParam = function.parameters[0]
        eventClass =
            eventParam.type.classifier as? KClass<*>
                ?: throw IllegalArgumentException("Event must be a class")
      }

      return SuspendClassInvoker(
          eventClass,
          listener,
          function,
          eventParam,
          instanceParam,
          annotation.priority,
          annotation.async,
      )
    }
  }
}
