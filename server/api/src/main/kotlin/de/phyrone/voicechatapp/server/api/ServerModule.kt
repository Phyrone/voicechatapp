package de.phyrone.voicechatapp.server.api

import org.atteo.classindex.IndexSubclasses
import org.koin.core.component.KoinComponent

/**
 * Marker interface for server modules all classes implementing this interface will be loaded by the
 * server on startup and add to the koin context
 *
 * @see IndexSubclasses
 */
interface ServerModule : KoinComponent
