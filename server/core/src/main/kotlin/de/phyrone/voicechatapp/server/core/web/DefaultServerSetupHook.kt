package de.phyrone.voicechatapp.server.core.web

import de.phyrone.voicechatapp.server.api.ServerModule
import de.phyrone.voicechatapp.server.api.Subscribe
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

class DefaultServerSetupHook : ServerModule {

  @Subscribe(priority = 10_000)
  fun WebSetupEvent.onSetup() {
    web {
      install(CORS) {
        allowMethod(HttpMethod.Head)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)

        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowSameOrigin = true
        allowCredentials = true

        allowXHttpMethodOverride()
        anyHost()
      }
      install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
      }
      routing { get("/") { call.respondText("Hello World") } }
    }
  }
}
