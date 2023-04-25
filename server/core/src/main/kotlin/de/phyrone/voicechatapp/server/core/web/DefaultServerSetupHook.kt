package de.phyrone.voicechatapp.server.core.web

import de.phyrone.voicechatapp.BuildInfo
import de.phyrone.voicechatapp.CommonConst
import de.phyrone.voicechatapp.server.api.ServerModule
import de.phyrone.voicechatapp.server.api.Subscribe
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.partialcontent.PartialContent
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import java.time.Duration

class DefaultServerSetupHook : ServerModule {

  @Subscribe(WEB_SETUP_EVENT_PRIORITY)
  fun WebSetupEvent.onSetup() {
    web {
      install(AutoHeadResponse)
      install(ConditionalHeaders)
      install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
      }
      install(PartialContent) {
        // Maximum number of ranges that will be accepted from a HTTP request.
        // If the HTTP request specifies more ranges, they will all be merged into a single range.
        maxRangeCount = 10
      }
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
        BuildInfo.VERSION
        header(HttpHeaders.Server, "${CommonConst.NAME}/${BuildInfo.VERSION}")
        header("X-Engine", "Ktor") // will send this header with each response
      }

      install(Compression) {
        gzip { priority = 1.0 }
        deflate {
          priority = 10.0
          minimumSize(1024) // condition
        }
      }
    }
  }

  companion object {

    const val WEB_SETUP_EVENT_PRIORITY = 1_000_000
  }
}
