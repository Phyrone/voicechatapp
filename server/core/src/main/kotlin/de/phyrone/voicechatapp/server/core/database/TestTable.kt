package de.phyrone.voicechatapp.server.core.database

import org.jetbrains.exposed.dao.id.UUIDTable

@AutoloadTable class TestTable : UUIDTable("test_1")
