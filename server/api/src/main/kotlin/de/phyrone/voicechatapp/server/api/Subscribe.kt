package de.phyrone.voicechatapp.server.api

/**
 * Marks a function as an Event Listener the function must have one parameter which is the event
 * class but can be extended f.e. EventClass.myListener(){} is valid myListener(EventClass){} is
 * valid
 *
 * @property priority the priority of the listener (higher = earlier)
 * @property async if the listener should be executed async or more specific if the following listeners do not wait for this listener to finish
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Subscribe(
    val priority: Int = 0,
    val async: Boolean = false,
)
