package de.phyrone.voicechatapp.server.core.pubsub

import java.io.Closeable
import kotlinx.coroutines.flow.Flow

interface PubSubBroker : Closeable {

  suspend fun publish(channel: String, message: Any)
  suspend fun subscribe(channel: String): Subscription
  interface Subscription : Closeable, Flow<Any>
}
