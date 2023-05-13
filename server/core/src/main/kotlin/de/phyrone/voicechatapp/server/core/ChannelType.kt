package de.phyrone.voicechatapp.server.core

enum class ChannelType(
    val allowOutsideSpace: Boolean,
) {
  TEXT(true), // pm text channels allow voice calls
  VOICE(false),
  DUMMY(false),
  ANNOUNCEMENT(true), // system anonymous channel is not part of any space
  FEED(false),
  STAGE(false),
  ;

  companion object {
    val ALLOWED_OUTSIDE_SPACE by lazy { values().filter { it.allowOutsideSpace } }
  }
}
