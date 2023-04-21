package de.phyrone.voicechatapp.server.api

import kotlin.reflect.KClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update

object ObjectWaiter {
    private val waiterInstances = MutableStateFlow(emptyList<Any>())

    /**
     * Waits for an instance of the given [clazz] to be ready
     *
     * @param clazz the class to wait for
     * @return the first instance which is ready
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun <T : Any> await(clazz: KClass<T>): T =
        waiterInstances
            .mapNotNull { candidates -> candidates.find { candidate -> clazz.isInstance(candidate) } }
            .first() as T

    /**
     * Marks an instance as ready
     *
     * @param instance the instance to mark as ready
     * @return [Unit] nothing its a procedure
     */
    operator fun invoke(instance: Any) = ready(instance)

    /**
     * Marks an instance as ready
     *
     * @param instance the instance to mark as ready
     * @return [Unit] nothing its a procedure
     */
    fun ready(instance: Any) {
        waiterInstances.update { instances -> instances + instance }
    }
}
