package de.phyrone.voicechatapp.server.main

import com.github.lalyos.jfiglet.FigletFont
import de.phyrone.voicechatapp.BuildInfo
import de.phyrone.voicechatapp.CommonConst
import de.phyrone.voicechatapp.server.DefaultAutoloader
import de.phyrone.voicechatapp.server.KoinApplicationLogger
import de.phyrone.voicechatapp.server.KoinFileSelector
import de.phyrone.voicechatapp.server.api.AutoloadSubscriber
import de.phyrone.voicechatapp.server.api.Autoloader
import de.phyrone.voicechatapp.server.api.EventBus
import de.phyrone.voicechatapp.server.api.ServerModule
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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.atteo.classindex.ClassIndex
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
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
                  val registerJOB = launch { registerModules(koin.get(), koin.get(), koin.get()) }
                  val eventBusFuture = async { koin.get<EventBus>() }
                  registerJOB.join()
                  val eventBus = eventBusFuture.await()
                  registerListeners(koin.get(), koin.get(), eventBus)

                  eventBus.post(ServerBootstrapEvent(), false)

                  rtBean = koin.get<RuntimeMXBean>()
                } catch (e: Throwable) {
                  LOGGER.atSevere().withCause(e).log("Error while bootstrapping")
                  exitProcess(1)
                }
              }
              .nanoseconds
      val operatingSystemInfo = koin.get<OperatingSystem>()

      val asciiArtwork: String

      try {
        asciiArtwork = FigletFont.convertOneLine(CommonConst.NAME)
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
              asciiArtwork,
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

      println()
    }
    LOGGER.atFine().log("Bootstrap Enqueued to Mainloop")
  }

  private fun registerModules(
      classloader: JarClassloader,
      instancer: DefaultSingletonInstancer,
      koin: Koin,
  ) {
    val serverModuleClasses: List<Class<*>>

    val indexTime =
        measureNanoTime {
              serverModuleClasses =
                  ClassIndex.getSubclasses(ServerModule::class.java, classloader).toList()
            }
            .nanoseconds
    LOGGER.atInfo().log("Found %d Modules (%s)", serverModuleClasses.size, indexTime)

    koin.loadModules(
        listOf(
            module(true) {
              for (serverModuleClass in serverModuleClasses) {
                single(TypeQualifier(serverModuleClass.kotlin)) {
                  instancer[serverModuleClass]
                } binds
                    arrayOf(
                        serverModuleClass.kotlin,
                        ServerModule::class,
                    )
              }
            },
        ),
    )
  }

  private suspend fun registerListeners(koin: Koin, autoloader: Autoloader, eventBus: EventBus) {
    coroutineScope {
      (koin.getAll<ServerModule>() + autoloader.getAnnotated(AutoloadSubscriber::class.java))
          .distinct()
          .forEach { listener ->
            eventBus.register(
                listener,
            )
          }
    }
  }

  companion object {
    private val LOGGER = logger()
  }
}
