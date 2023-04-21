package de.phyrone.voicechatapp.server.main

import com.github.lalyos.jfiglet.FigletFont
import de.phyrone.voicechatapp.BuildInfo
import de.phyrone.voicechatapp.CommonConst
import de.phyrone.voicechatapp.server.DefaultAutoloader
import de.phyrone.voicechatapp.server.KoinApplicationLogger
import de.phyrone.voicechatapp.server.KoinFileSelector
import de.phyrone.voicechatapp.server.api.AutoloadListener
import de.phyrone.voicechatapp.server.api.Autoloader
import de.phyrone.voicechatapp.server.api.EventBus
import de.phyrone.voicechatapp.server.api.Prefetch
import de.phyrone.voicechatapp.server.api.event.ServerBootstrapEvent
import de.phyrone.voicechatapp.server.api.formatFancy
import de.phyrone.voicechatapp.server.api.logger
import de.phyrone.voicechatapp.server.eventbus.DefaultEventBus
import de.phyrone.voicechatapp.server.plugins.DefaultSingletonInstancer
import de.phyrone.voicechatapp.server.plugins.JarClassloader
import de.phyrone.voicechatapp.server.plugins.PluginLoader
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import java.time.Instant
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import oshi.SystemInfo
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

class ServerBootstrap(
    private val startupParameters: StartupParameters,
) : Runnable {

  private fun createKoin() =
      startKoin {
            logger(KoinApplicationLogger())
            modules(
                module(true) {
                  single { this@startKoin }
                  single { get<KoinApplication>().koin }
                  single { startupParameters }
                  single(named(KoinFileSelector.MAIN_CONFIG_FILE)) {
                    get<StartupParameters>().configFile
                  }
                  single(named(KoinFileSelector.PLUGINS_DIR)) {
                    get<StartupParameters>().configFile
                  }
                  single { DefaultSingletonInstancer(get()) }
                  single { DefaultEventBus() } bind EventBus::class
                  single { ServerShutdown(get()) }
                  single { JarClassloader(this@ServerBootstrap::class.java.classLoader) } bind
                      ClassLoader::class
                  single { DefaultAutoloader(get(), get()) } bind Autoloader::class
                  single { PluginLoader(get<JarClassloader>()) }
                  single { ManagementFactory.getRuntimeMXBean() }
                  single { ManagementFactory.getMemoryMXBean() }
                  single { ManagementFactory.getThreadMXBean() }
                  // SystemInfo
                  single { SystemInfo() }
                  single { get<SystemInfo>().operatingSystem }
                  single { get<SystemInfo>().hardware }
                  single { get<OperatingSystem>().fileSystem }
                  single { get<OperatingSystem>().networkParams }
                  single { get<HardwareAbstractionLayer>().memory }
                  single { get<HardwareAbstractionLayer>().computerSystem }
                  single { get<HardwareAbstractionLayer>().processor }
                },
            )
          }
          .also { it.createEagerInstances() }

  override fun run() {
    CoroutineScope(Dispatchers.Main).launch {
      LOGGER.atInfo().log("Starting Server...")
      val koin: Koin
      val rtBean: RuntimeMXBean
      val bootstrapTime =
          measureNanoTime {
                try {
                  val koinApplication = createKoin()
                  koin = koinApplication.koin
                  koin.get<ServerShutdown>().register()
                  koin.get<PluginLoader>().loadPlugins(/* TODO add configurable folder */ )
                  prefetchInstances(koin.get())
                  val eventBus = koin.get<EventBus>()
                  registerListeners(koin.get(), eventBus)
                  withContext(Dispatchers.Default) { eventBus.post(ServerBootstrapEvent(), false) }
                  rtBean = koin.get<RuntimeMXBean>()
                } catch (e: Throwable) {
                  LOGGER.atSevere().withCause(e).log("Error while bootstrapping")
                  exitProcess(1)
                }
              }
              .nanoseconds
      val operatingSystemInfo = koin.get<OperatingSystem>()

      val asciArtwork: String

      try {
        asciArtwork = FigletFont.convertOneLine(CommonConst.NAME)
      } catch (e: Exception) {
        LOGGER.atSevere().withCause(e).log("Error while loading ASCII Artwork")
        throw e
      }

      LOGGER.atInfo()
          .log(
              "Server Started (%s)! (JVM is running for %s now)%n%s" +
                  "  Version: %s%n" +
                  "  Build: %s - %s (%s)%n" +
                  "  Java: %s - %s (%s)%n" +
                  "  OS: %s %s [%s]%n",
              /* Timings */
              bootstrapTime,
              rtBean.uptime.milliseconds,
              /* ASCII */
              asciArtwork,
              /* Version */
              BuildInfo.VERSION,
              /* Build */
              BuildInfo.BIT_COMMIT_BRANCH,
              BuildInfo.GIT_COMMIT_HASH,
              Instant.ofEpochMilli(BuildInfo.BUILD_TIMESTAMP_MILLIS).formatFancy(),
              /* Java */
              rtBean.vmVendor,
              rtBean.vmName,
              rtBean.vmVersion,
              /* OS */
              operatingSystemInfo.family,
              operatingSystemInfo.versionInfo,
              operatingSystemInfo.manufacturer,
          )
    }
    LOGGER.atFine().log("Bootstrap Enqueued to Mainloop")
  }

  private suspend fun prefetchInstances(
      autoloader: DefaultAutoloader,
  ) = coroutineScope {
    LOGGER.atInfo().log("Prefetching...")
    val earlyIndexTime = measureNanoTime { autoloader.getAnnotated(Prefetch::class) }.nanoseconds
    LOGGER.atFine().log("Prefetch done (%s)", earlyIndexTime)
  }

  private suspend fun registerListeners(autoloader: Autoloader, eventBus: EventBus) {
    coroutineScope {
      autoloader.getAnnotated(AutoloadListener::class).forEach { listener ->
        eventBus.register(listener)
      }
    }
  }

  companion object {
    private val LOGGER = logger()
  }
}
