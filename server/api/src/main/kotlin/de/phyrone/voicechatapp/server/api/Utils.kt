/** This file contains numerus little helper functions and classes */

@file:JvmName("Utils")
@file:JvmMultifileClass

package de.phyrone.voicechatapp.server.api

import com.google.common.flogger.FluentLogger
import com.google.common.flogger.backend.LoggerBackend
import com.google.common.flogger.backend.Platform
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

private val fluentLoggerConstructor by lazy {
    FluentLogger::class.java.getDeclaredConstructor(LoggerBackend::class.java).apply {
        isAccessible = true
    }
}

/**
 * Formats the [Instant] to a fancy String with the current TimeZone f.e. 01.01.2021 23:59:59
 * [Coordinated Universal Time]
 *
 * @receiver the [Instant] to format
 * @return a fancy formatted String of the [Instant] with the current TimeZone f.e. 01.01.2021
 *   23:59:59 [Coordinated Universal Time]
 */
fun Instant.formatFancy(): String {
    val zoneId = ZoneId.systemDefault()
    val timeZone =
        TimeZone.getTimeZone(zoneId).getDisplayName(true, TimeZone.LONG, Locale.getDefault())
    val dateToFormat = LocalDateTime.ofInstant(this, zoneId)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

    return String.format("%s [%s]", formatter.format(dateToFormat), timeZone)
}

/**
 * Creates a [FluentLogger] for the class where this function is called It is basically an shorter
 * alias for [FluentLogger.forEnclosingClass]
 *
 * @return the corresponding [FluentLogger]
 * @see FluentLogger.forEnclosingClass
 */
@Suppress("NOTHING_TO_INLINE") // needs to be inlined to get the correct class name (stacktrace)
inline fun logger(): FluentLogger = FluentLogger.forEnclosingClass()

/**
 * Creates a [FluentLogger] for the given [name]
 *
 * @param name the name of the logger
 * @return the corresponding [FluentLogger]
 */
fun logger(name: String): FluentLogger {
    val backend = Platform.getBackend(name)
    return fluentLoggerConstructor.newInstance(backend)
}
/*
class SharableMutex {

  private val lockQueue = MutableStateFlow(emptyList<Claim>())
  suspend fun lock(exclusive: Boolean) {
    val claim = Claim(exclusive)
    lockQueue.update { it + claim }
    lockQueue.first { canObtain(claim, it) }
  }

  private fun canObtain(claim: Claim, list: List<Claim>): Boolean {
    val exclusive = claim.exclusive
    return if (exclusive) {
      // exclusive claim can only obtain if it is the first in the queue
      list.indexOf(claim) == 0
    } else {
      // check if exclusive claim is in front of us and if not we can obtain
      val firstExclusive = list.indexOfFirst { it.exclusive }
      val ownIndex = list.indexOf(claim)
      firstExclusive == -1 || firstExclusive > ownIndex
    }
  }

  fun unlock() {
    lockQueue.update { it.drop(1) }
  }

  suspend fun withLock(exclusive: Boolean, block: () -> Unit) {
    lock(exclusive)
    try {
      block()
    } finally {
      unlock()
    }
  }

  private class Claim(
      val exclusive: Boolean,
  )
}

 */
