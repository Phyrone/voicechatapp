package de.phyrone.voicechatapp.server.core.database.entities

import de.phyrone.voicechatapp.server.core.database.tables.AccountsTable
import de.phyrone.voicechatapp.server.core.database.tables.ProfilesTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AccountEntity(id: EntityID<Long>) : LongEntity(id) {

  val profiles by ProfileEntity optionalReferrersOn ProfilesTable.account
  companion object : LongEntityClass<AccountEntity>(AccountsTable)
}
