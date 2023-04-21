package de.phyrone.voicechatapp.server.api

import org.atteo.classindex.IndexAnnotated

/**
 * Marks a class as a Listener that should be loaded to the instanced
 * [de.phyrone.voicechatapp.server.api.EventBus] this is executed during bootstrap
 */
@IndexAnnotated
@Target(AnnotationTarget.CLASS)
annotation class AutoloadListener
