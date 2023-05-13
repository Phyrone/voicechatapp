package de.phyrone.voicechatapp.server.core.database.tables

import de.phyrone.voicechatapp.server.core.DOMAIN_MAX_LENGTH
import de.phyrone.voicechatapp.server.core.USER_NICKNAME_MAX_LENGTH
import de.phyrone.voicechatapp.server.core.database.types.UIDTable

@AutoloadTable
object ProfilesTable : UIDTable("profile") {

  val account =
      reference("account", AccountsTable).nullable() // if null its sourced by a third party server

  val domain = varchar("domain", DOMAIN_MAX_LENGTH)
  val username = varchar("username", USER_NICKNAME_MAX_LENGTH)

  // similar to discord deprecated system but with up to 16 chars depending on demand
  // by default probably 4 chars but can be increased if needed
  val discriminator =
      varchar("discriminator", 16)
          .nullable() // probably not allowed to have a username without discriminator

  val displayname =
      varchar("displayname", USER_NICKNAME_MAX_LENGTH).nullable() // null = show username

  init {
    uniqueIndex("INDEX_PROFILE_IDENTIFIER", domain, username, discriminator)
  }
}
