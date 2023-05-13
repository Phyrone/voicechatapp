package de.phyrone.voicechatapp

import java.time.Instant

private val BUILD_TIMESTAMP_INSTANT: Instant by lazy { Instant.parse(BuildInfo.BUILD_TIMESTAMP) }
val BuildInfo.BUILD_TIMESTAMP_INSTANT: Instant
  get() = de.phyrone.voicechatapp.BUILD_TIMESTAMP_INSTANT