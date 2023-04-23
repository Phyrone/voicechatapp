package de.phyrone.voicechatapp.server.core.database.types


import de.phyrone.voicechatapp.server.core.database.UID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID

open class UidEntity(id: EntityID<UID>) : Entity<UID>(id)