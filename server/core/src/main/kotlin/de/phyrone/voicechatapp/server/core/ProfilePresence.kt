package de.phyrone.voicechatapp.server.core

enum class ProfilePresence {
  ONLINE, // green (default)
  OFFLINE, // or appear offline doesnt matter //grey
  DO_NOT_DISTURB, // red
  AWAY, // yellow
  ACTIVE, // blue
  ;

  val id = ordinal
}
