package de.phyrone.voicechatapp.server.core.web

import de.phyrone.voicechatapp.server.api.EventBus
import de.phyrone.voicechatapp.server.api.ServerModule
import de.phyrone.voicechatapp.server.api.Subscribe
import de.phyrone.voicechatapp.server.api.event.ServerBootstrapEvent
import de.phyrone.voicechatapp.server.api.event.ServerShutdownEvent
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class WebModule : ServerModule {

  private val server by inject<ApplicationEngine>()
  private val eventBus by inject<EventBus>()

  @Subscribe(async = true)
  fun ServerBootstrapEvent.onBootstrap() {
    loadKoinModules(
        module {
          single { embeddedServer(Netty, get()) } bind ApplicationEngine::class
          single {
            applicationEngineEnvironment {
              connector { port = 8080 }
              runBlocking { eventBus.post(WebSetupEvent(this@applicationEngineEnvironment)) }
            }
          } bind ApplicationEngineEnvironment::class
        },
    )
    server.start(wait = false)
  }

  @Subscribe(async = true)
  fun ServerShutdownEvent.onShutdown() {
    server.stop(300, 500)
  }

  companion object {}
}
