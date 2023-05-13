package de.phyrone.voicechatapp.server.core.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import io.nats.client.Connection
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector

class NatsPubSubBroker(
    private val natsConnection: Connection,
    private val mapper: ObjectMapper,
) : PubSubBroker {

  private val dispatcher = natsConnection.createDispatcher()

  override suspend fun publish(channel: String, message: Any) {
    natsConnection.publish(channel, mapper.writeValueAsBytes(message))
  }

  override suspend fun subscribe(channel: String): PubSubBroker.Subscription {
    return NatsSubscription(channel)
  }

  private inner class NatsSubscription(
      channel: String,
  ) : PubSubBroker.Subscription {
    private val eventChannel = Channel<Any>(Channel.UNLIMITED)
    private val sub = dispatcher.subscribe(channel) { message -> handleMessage(message.data) }

    private fun handleMessage(data: ByteArray) {
      val message = mapper.readValue(data, Any::class.java)
      eventChannel.trySend(message)
      // TODO on failure
    }

    override suspend fun collect(collector: FlowCollector<Any>) {
      for (message in eventChannel) {
        collector.emit(message)
      }
    }

    override fun close() {
      eventChannel.close()
      sub.unsubscribe()
    }
  }

  override fun close() {
    natsConnection.close()
  }
}
