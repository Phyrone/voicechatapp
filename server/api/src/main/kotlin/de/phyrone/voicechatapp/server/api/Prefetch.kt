package de.phyrone.voicechatapp.server.api

import org.atteo.classindex.IndexAnnotated

/**
 * Marks a class as a Listener that should be loaded to the instanced early during bootstrap the
 * instace is done in parralel to other [Prefetch]s this is executed during bootstrap
 */
@IndexAnnotated
@Target(AnnotationTarget.CLASS)
annotation class Prefetch
