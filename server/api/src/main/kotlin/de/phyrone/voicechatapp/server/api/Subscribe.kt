package de.phyrone.voicechatapp.server.api

/*
 * @param priority the priority of the listener (higher = earlier)
 * @param async if the listener should be executed async
 * THIS ONLY MEANS THAT FOLLOWING LISTENERS DO NOT WAIT FOR THIS LISTENER TO FINISH
 * @param ignoreCancelled if the listener should be executed if the event is cancelled (not implemented yet)
 */

/**
 * Marks a function as an Event Listener the function must have one parameter which is the event
 * class but can be extended f.e. EventClass.myListener(){} is valid myListener(EventClass){} is
 * valid
 *
 * @property priority
 * @property async
 * @property ignoreCancelled
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Subscribe(
    val priority: Int = 0,
    val async: Boolean = false,
    val ignoreCancelled: Boolean = false,
)
