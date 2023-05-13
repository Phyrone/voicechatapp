package de.phyrone.voicechatapp.server.core.database.entities

import de.phyrone.voicechatapp.server.core.database.UID
import de.phyrone.voicechatapp.server.core.database.tables.ChannelsTable
import de.phyrone.voicechatapp.server.core.database.types.UIDEntity
import de.phyrone.voicechatapp.server.core.database.types.UIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ChannelEntity(id: EntityID<UID>) : UIDEntity(id) {

  val parent by ChannelEntity optionalReferencedOn ChannelsTable.parent
  val children by ChannelEntity optionalReferrersOn ChannelsTable.parent

  companion object : UIDEntityClass<ChannelEntity>(ChannelsTable)
}
