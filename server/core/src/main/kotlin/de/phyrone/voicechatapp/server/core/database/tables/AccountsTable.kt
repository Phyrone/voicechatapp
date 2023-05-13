package de.phyrone.voicechatapp.server.core.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable

@AutoloadTable
object AccountsTable : LongIdTable("account") {

  // can be disabled by admin f.e. suspension/ban or secure in case of a hack
  val enabled = bool("enabled").default(true)

  val username = varchar("username", 64).uniqueIndex()

  /** Argon2 hash of the password including salt etc. */
  val password = text("password")

  val sessionSecret = binary("secret", 256).uniqueIndex()
}
