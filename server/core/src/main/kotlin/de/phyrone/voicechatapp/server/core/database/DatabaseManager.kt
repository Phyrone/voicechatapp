package de.phyrone.voicechatapp.server.core.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.phyrone.voicechatapp.server.api.Autoloader
import de.phyrone.voicechatapp.server.api.ObjectWaiter
import de.phyrone.voicechatapp.server.api.ServerModule
import de.phyrone.voicechatapp.server.api.Subscribe
import de.phyrone.voicechatapp.server.api.event.ServerBootstrapEvent
import de.phyrone.voicechatapp.server.api.event.ServerShutdownEvent
import de.phyrone.voicechatapp.server.api.logger
import de.phyrone.voicechatapp.server.core.CoreSqlLogger
import de.phyrone.voicechatapp.server.core.database.tables.AutoloadTable
import java.io.Closeable
import java.sql.Connection
import javax.sql.DataSource
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.KoinApplication
import org.koin.core.component.inject
import org.koin.dsl.bind
import org.koin.dsl.module

class DatabaseManager : ServerModule {

  private val koinApplication by inject<KoinApplication>()
  private val dataSource by inject<DataSource>()
  private val database by inject<Database>()
  private val autoloader by inject<Autoloader>()

  @Subscribe(async = true)
  suspend fun ServerBootstrapEvent.onBootstrap() {
    logger.atInfo().log("Migrating Database if needed...")
    val databaseModuleLoad =
        measureNanoTime {
              koinApplication.modules(DATABASE_MODULE)
              newSuspendedTransaction(
                  Dispatchers.IO,
                  database,
                  Connection.TRANSACTION_SERIALIZABLE,
              ) {
                migrateDatabase()
              }
            }
            .nanoseconds
    logger.atInfo().log("Migration Done! (%s)", databaseModuleLoad)
    ObjectWaiter(this)
  }

  private suspend fun migrateDatabase() {
    // migration scripts here

    val tables =
        SchemaUtils.sortTablesByReferences(
                autoloader.getAnnotated(AutoloadTable::class, Table::class),
            )
            .toTypedArray()
    SchemaUtils.createMissingTablesAndColumns(*tables, withLogs = true, inBatch = false)
  }

  @Subscribe
  fun ServerShutdownEvent.onShutdown() {
    logger.atInfo().log("Disabling Database Module...")
    val disableTime = measureNanoTime { (dataSource as? Closeable)?.close() }.nanoseconds
    logger.atInfo().log("Database Module Disabled (%s)", disableTime)
  }

  companion object {
    private val logger = logger()
    private val DATABASE_MODULE =
        module(true) {
          single { databaseConfig() }
          single { Database.connect(get<DataSource>(), databaseConfig = get<DatabaseConfig>()) }
          single { datasourceConfig() }
          single { HikariDataSource(get()) } bind DataSource::class
        }

    private fun databaseConfig(): DatabaseConfig {
      return DatabaseConfig {
        this.sqlLogger = CoreSqlLogger
        this.useNestedTransactions = true
      }
    }

    private fun datasourceConfig(): HikariConfig {
      // TODO configurable
      val config = HikariConfig()
      config.jdbcUrl = "jdbc:h2:./data/database;auto_server=true"
      config.driverClassName = "org.h2.Driver"
      config.maximumPoolSize = 15
      config.minimumIdle = 1
      return config
    }
  }
}
