package de.phyrone.voicechatapp.server.core.database.types

import de.phyrone.voicechatapp.server.core.database.UID
import org.jetbrains.exposed.sql.Table

fun Table.uid(name: String) = registerColumn<UID>(name, UIDColumnType())