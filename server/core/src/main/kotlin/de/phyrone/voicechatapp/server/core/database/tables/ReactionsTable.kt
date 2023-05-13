package de.phyrone.voicechatapp.server.core.database.tables

import de.phyrone.voicechatapp.server.core.database.types.UIDTable

@AutoloadTable
object ReactionsTable : UIDTable("reaction") {

  val displayname = varchar("displayname", 64)
}
