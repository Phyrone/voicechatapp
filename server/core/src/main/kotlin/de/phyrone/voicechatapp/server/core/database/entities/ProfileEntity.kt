package de.phyrone.voicechatapp.server.core.database.entities

import de.phyrone.voicechatapp.server.core.database.UID
import de.phyrone.voicechatapp.server.core.database.tables.ProfilesTable
import de.phyrone.voicechatapp.server.core.database.types.UIDEntity
import de.phyrone.voicechatapp.server.core.database.types.UIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProfileEntity(id: EntityID<UID>) : UIDEntity(id) {

  val account by AccountEntity optionalReferencedOn ProfilesTable.account
  companion object : UIDEntityClass<ProfileEntity>(ProfilesTable)
}
