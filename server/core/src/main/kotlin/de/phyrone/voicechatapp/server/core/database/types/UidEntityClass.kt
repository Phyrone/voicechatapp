package de.phyrone.voicechatapp.server.core.database.types

import de.phyrone.voicechatapp.server.core.database.UID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable

open class UidEntityClass<out T : Entity<UID>>(
    table: IdTable<UID>,
    entityType: Class<T>? = null,
    entityCtor: ((EntityID<UID>) -> T)? = null,
) : EntityClass<UID, T>(table, entityType, entityCtor)
