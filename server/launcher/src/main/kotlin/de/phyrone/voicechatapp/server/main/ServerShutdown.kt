package de.phyrone.voicechatapp.server.main

import de.phyrone.voicechatapp.server.api.EventBus
import de.phyrone.voicechatapp.server.api.event.ServerShutdownEvent
import de.phyrone.voicechatapp.server.api.logger
import kotlin.concurrent.thread
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds
import kotlinx.coroutines.runBlocking
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ServerShutdown(private val koin: Koin) : KoinComponent {
  override fun getKoin(): Koin = koin

  private val eventBus by inject<EventBus>()

  fun register() {
    Runtime.getRuntime()
        .addShutdownHook(
            thread(false, name = "shutdown", priority = Thread.MAX_PRIORITY) { shutdown() },
        )
  }

  private fun shutdown() = runBlocking {
    LOGGER.atInfo().log("Shutting down...")
    val shutdownTime = measureNanoTime { eventBus.post(ServerShutdownEvent(), true) }.nanoseconds
    LOGGER.atInfo().log("Shutdown done! (%s)", shutdownTime)
  }

  companion object {
    private val LOGGER = logger()
  }
}
