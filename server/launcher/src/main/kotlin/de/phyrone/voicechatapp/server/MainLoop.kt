package de.phyrone.voicechatapp.server

import de.phyrone.voicechatapp.server.api.logger
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.Runnable

object MainLoop : Executor, MainCoroutineDispatcher() {
  private val LOGGER = logger()

  private val queue = LinkedBlockingQueue<Runnable>()

  internal fun run() {
    LOGGER.atInfo().log("Starting MainLoop...")
    try {
      // time logging has some overhead and can be disabled of not logged
      val logTime = LOGGER.atFinest().isEnabled

      while (true) {
        try {
          val task = queue.take()
          LOGGER.atFinest().log("GOT NEW TASK: %s -> EXECUTING LOG_TIME=%s", task, logTime)
          if (logTime) {
            val time = measureNanoTime { task.run() }.nanoseconds
            LOGGER.atFinest().log("TASK FINISHED: %s (%s)", task, logTime)
          } else {
            task.run()
            LOGGER.atFinest().log("TASK FINISHED: %s (time not measured)", task)
          }
        } catch (e: InterruptedException) {
          throw e
        } catch (e: Exception) {
          LOGGER.atSevere().withCause(e).log("Error in MainLoop")
        }
      }
    } finally {
      LOGGER.atInfo().log("Stopping MainLoop...")
    }
  }

  override fun execute(command: Runnable) {
    queue.add(command)
  }

  override val immediate: MainCoroutineDispatcher
    get() = this

  override fun dispatch(context: CoroutineContext, block: Runnable) {
    queue.add(block)
  }
}
