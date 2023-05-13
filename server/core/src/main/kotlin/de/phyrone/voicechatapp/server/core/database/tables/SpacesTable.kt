package de.phyrone.voicechatapp.server.core.database.tables

import de.phyrone.voicechatapp.server.core.database.types.UIDTable

object SpacesTable : UIDTable("space") {
  val owner = reference("owner", ProfilesTable)
}
