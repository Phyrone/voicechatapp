package de.phyrone.voicechatapp.server.api

import org.atteo.classindex.IndexAnnotated

/**
 * Classes annotated with this annotation will be loaded by the server on startup and add to the
 * [EventBus]
 *
 * @see IndexAnnotated
 */
@IndexAnnotated
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class AutoloadSubscriber
