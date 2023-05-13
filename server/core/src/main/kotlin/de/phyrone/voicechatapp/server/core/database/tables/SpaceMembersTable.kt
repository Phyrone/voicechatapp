package de.phyrone.voicechatapp.server.core.database.tables

import de.phyrone.voicechatapp.server.core.database.types.UIDTable

@AutoloadTable
object SpaceMembersTable : UIDTable("space_membership") {
  val space = reference("space", SpacesTable)
  val profile = reference("profile", ProfilesTable)
  // TODO roles

  init {
    uniqueIndex("INDEX_SPACE_MEMBERSHIP", space, profile)
  }
}
