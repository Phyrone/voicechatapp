package de.phyrone.voicechatapp.server

import de.phyrone.voicechatapp.server.api.logger
import java.util.logging.Level as JavaUtilLoggingLevel
import org.koin.core.logger.Level as KoinLevel
import org.koin.core.logger.Logger as KoinLoggerLogger
import org.koin.core.logger.MESSAGE

class KoinApplicationLogger :
    KoinLoggerLogger(
        when {
          KOIN_LOGGER.atFiner().isEnabled -> KoinLevel.DEBUG
          KOIN_LOGGER.atFine().isEnabled -> KoinLevel.INFO
          KOIN_LOGGER.atWarning().isEnabled -> KoinLevel.WARNING
          KOIN_LOGGER.atSevere().isEnabled -> KoinLevel.ERROR
          else -> KoinLevel.NONE
        },
    ) {
  override fun display(level: KoinLevel, msg: MESSAGE) {
    KOIN_LOGGER.at(level.toJavaUtilLogLevel()).log(msg)
  }

  companion object {
    private val KOIN_LOGGER = logger("Koin")

    fun KoinLevel.toJavaUtilLogLevel(): JavaUtilLoggingLevel =
        when (this) {
          KoinLevel.DEBUG -> JavaUtilLoggingLevel.FINER
          KoinLevel.INFO -> JavaUtilLoggingLevel.FINE
          KoinLevel.WARNING -> JavaUtilLoggingLevel.WARNING
          KoinLevel.ERROR -> JavaUtilLoggingLevel.SEVERE
          KoinLevel.NONE -> JavaUtilLoggingLevel.OFF
        }
  }
}
