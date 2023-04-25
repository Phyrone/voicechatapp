package de.phyrone.voicechatapp.server.core

import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.cacheControl
import io.ktor.server.response.respondTextWriter
import java.io.Writer
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow

data class SseEvent(val data: String, val event: String? = null, val id: String? = null)

fun Writer.sendSSE(event: SseEvent) {
  if (event.id != null) {
    write("id: ${event.id}\n")
  }
  if (event.event != null) {
    write("event: ${event.event}\n")
  }

  for (dataLine in event.data.lines()) {
    write("data: $dataLine\n")
  }
  write("\n")
  flush()
}

suspend fun ApplicationCall.respondSSE(events: Iterable<SseEvent>) {
  response.cacheControl(CacheControl.NoCache(null))
  respondTextWriter(contentType = ContentType.Text.EventStream) {
    for (event in events) sendSSE(event)
  }
}

suspend fun ApplicationCall.respondSSE(events: Flow<SseEvent>) {
  response.cacheControl(CacheControl.NoCache(null))
  respondTextWriter(contentType = ContentType.Text.EventStream) {
    events.collect { event -> sendSSE(event) }
  }
}

suspend fun ApplicationCall.respondSSE(events: ReceiveChannel<SseEvent>) {
  response.cacheControl(CacheControl.NoCache(null))
  respondTextWriter(contentType = ContentType.Text.EventStream) {
    for (event in events) sendSSE(event)
  }
}
