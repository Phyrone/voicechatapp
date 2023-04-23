package de.phyrone.voipsys.server.database.types

import de.phyrone.voicechatapp.server.core.database.UID
import de.phyrone.voicechatapp.server.core.database.types.uid
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

open class UIDTable(name: String = "") : IdTable<UID>(name) {
  final override val id: Column<EntityID<UID>> = uid("uid").entityId()
  final override val primaryKey = PrimaryKey(id)
}
