package de.phyrone.voicechatapp.server.core

import de.phyrone.voicechatapp.server.api.logger
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.TransactionManager

object CoreSqlLogger : SqlLogger {
    private val LOGGER = logger("SQL")
    override fun log(context: StatementContext, transaction: Transaction) {
        LOGGER.atFine().log("%s", context.expandArgs(TransactionManager.current()))
    }
}