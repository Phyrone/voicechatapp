package de.phyrone.voicechatapp.server.core.pubsub

import de.phyrone.voicechatapp.server.api.ServerModule

class PubSubManager : ServerModule, PubSubBroker {

  private val localBroker = LocalPubSubBroker()
  override suspend fun publish(channel: String, message: Any) {
    localBroker.publish(channel, message)
  }

  override suspend fun subscribe(channel: String): PubSubBroker.Subscription {
    return localBroker.subscribe(channel)
  }

  override fun close() {}
}
