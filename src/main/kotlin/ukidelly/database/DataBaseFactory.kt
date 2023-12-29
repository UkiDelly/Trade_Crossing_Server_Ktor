package ukidelly.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ukidelly.database.models.comment.TradeFeedComments
import ukidelly.database.models.like.TradeFeedLikes
import ukidelly.database.models.post.TradeFeeds
import ukidelly.database.models.user.Users
import ukidelly.database.tables.*


object DataBaseFactory {

    lateinit var database: Database

    fun init(databaseUrl: String, user: String, driver: String, password: String) {
        database =
            Database.connect(
                createHikariDataSource(databaseUrl, driver, user, password),
                databaseConfig = DatabaseConfig.invoke { useNestedTransactions = true }
            )

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.createMissingTablesAndColumns(
                Users,
                TradeFeeds,
                TradeFeedComments,
                TradeFeedLikes,
                Feeds,
                FeedComments,
                FeedLikes,
                FeedImages,
                Images,
                withLogs = false,
                inBatch = true
            )
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO, database) {
            addLogger(StdOutSqlLogger)
            block()
        }

    private fun createHikariDataSource(url: String, driver: String, user: String, password: String) = HikariDataSource(
        HikariConfig().apply {
            driverClassName = driver
            username = user
            setPassword(password)
            jdbcUrl = url
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
    )
}