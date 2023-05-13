package de.phyrone.voicechatapp.server.core.pubsub

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class LocalPubSubBroker : PubSubBroker {
  private val localFlow = MutableSharedFlow<Pair<String, Any>>()

  override fun close() {}

  override suspend fun publish(channel: String, message: Any) {
    localFlow.emit(channel to message)
  }

  override suspend fun subscribe(channel: String): PubSubBroker.Subscription {
    val subscriptionFlow = localFlow.filter { it.first == channel }.map { it.second }
    return LocalSubscription(subscriptionFlow)
  }

  private inner class LocalSubscription(
      flow: Flow<Any>,
  ) : PubSubBroker.Subscription, Flow<Any> by flow {
    override fun close() {}
  }
}
