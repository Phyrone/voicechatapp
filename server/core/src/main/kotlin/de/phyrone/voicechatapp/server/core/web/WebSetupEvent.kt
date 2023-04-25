package de.phyrone.voicechatapp.server.core.web

import io.ktor.server.application.Application
import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder
import io.ktor.server.engine.applicationEngineEnvironment

class WebSetupEvent(
    private val applicationEngineEnvironment: ApplicationEngineEnvironmentBuilder,
) {
  fun appEnvironment(block: ApplicationEngineEnvironmentBuilder.() -> Unit) =
      block(applicationEngineEnvironment)
  fun web(block: Application.() -> Unit) = applicationEngineEnvironment.module { block() }
}
