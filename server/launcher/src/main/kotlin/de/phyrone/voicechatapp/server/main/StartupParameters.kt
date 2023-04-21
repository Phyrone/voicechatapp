package de.phyrone.voicechatapp.server.main

import com.google.common.flogger.FluentLogger
import de.phyrone.voicechatapp.BuildInfo
import de.phyrone.voicechatapp.server.MainLoop
import java.io.File
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import picocli.CommandLine.Command
import picocli.CommandLine.Option

/**
 * This class contains the startup parameters of the server It is created by the [CommandLine] class
 * of the Picocli library which parsed the properties from startup arguments
 */
@Command(
    name = "server",
    mixinStandardHelpOptions = true,
    version = [BuildInfo.VERSION],
)
class StartupParameters : Runnable {

  @Option(
      names = ["--config", "-c"],
      description = ["The config file to use"],
      required = false,
  )
  var configFile: File = File("config.yml")

  override fun run() {
    LOGGER.atInfo()
        .log("%s", ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE))

    ServerBootstrap(this).run()
    MainLoop.run()
  }

  companion object {
    private val LOGGER = FluentLogger.forEnclosingClass()
  }
}
