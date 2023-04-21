package de.phyrone.voicechatapp.server.api

import kotlin.reflect.KClass

/**
 * This is the EventBus for the Server
 *
 * Events are used to communicate between the different modules and maintain the state of the server
 * and compoents
 *
 * Events are posted to the EventBus and can be received by any Listener that is registered to the
 * EventBus
 */
interface EventBus {
    /**
     * Posts an event to the EventBus and calls all registered Listeners that are listening for this
     * event or its superclasses
     *
     * @param event the event to post to the EventBus all registered Listeners will be called with
     *   this event as parameter
     * @param gracefulErrorHandling if true errors will be logged but not thrown to the caller of this
     *   function if false errors will be thrown to the caller of this function following eveents mig
     * @throws any Exception if an error occurs and gracefulErrorHandling is false and an error occurs
     *   during the execution of a listener
     */
    suspend fun post(event: Any, gracefulErrorHandling: Boolean = true)

    /**
     * Waits for the next event to be posted to the EventBus and returns it
     *
     * @param event the event class to wait for
     * @return the event that was posted to the EventBus
     */
    suspend infix fun <T : Any> await(event: KClass<T>): T

    /**
     * Registers a Listener to the EventBus
     *
     * All functions that are annotated with [Subscribe] will be called when an event is posted to the
     * EventBus
     * - Functions can be suspend
     *
     * @param listener the listener to register
     */
    fun register(listener: Any)

    /**
     * Unregisters a Listener from the EventBus unregistering a listener will remove all [Subscribe]
     * functions from the listener unregistering a listener that is not registered will do nothing
     *
     * @param listener the listener to unregister
     */
    fun unregister(listener: Any)

    /**
     * Checks if a Listener is registered to the EventBus
     *
     * @param listener the listener to check
     * @return true if the listener is registered false if not
     */
    fun isRegistered(listener: Any): Boolean

    /** Unregisters all registered Listeners from the EventBus */
    fun unregisterAll()

    /**
     * Gets all registered Listeners
     *
     * @return a list of all registered Listeners
     */
    fun getRegisteredListeners(): List<Any>
}
