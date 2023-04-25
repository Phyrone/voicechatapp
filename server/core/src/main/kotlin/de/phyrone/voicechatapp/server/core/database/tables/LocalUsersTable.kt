package de.phyrone.voicechatapp.server.core.database.tables

import org.jetbrains.exposed.dao.id.LongIdTable

@AutoloadTable
object LocalUsersTable : LongIdTable("local_user") {
  val user = reference("user", UsersTable)

  val username = varchar("username", 128).uniqueIndex()

  /** Argon2 hash of the password including salt etc. */
  val password = text("password")

  val sessionSecret = varchar("secret", 256).uniqueIndex()
}
