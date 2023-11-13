package ukidelly.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ukidelly.database.models.comment.TradeFeedComments
import ukidelly.database.models.like.TradeFeedLikes
import ukidelly.database.models.post.TradeFeeds
import ukidelly.database.models.user.Users
import ukidelly.database.tables.FeedComments
import ukidelly.database.tables.FeedImages
import ukidelly.database.tables.FeedLikes
import ukidelly.database.tables.Feeds


object DataBaseFactory {

    lateinit var database: Database

    fun init(databaseUrl: String, user: String, driver: String, password: String) {
        database =
            Database.connect(
                createHikariDataSource(databaseUrl, driver, user, password),
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
                withLogs = false,
                inBatch = true
            )
        }
    }

    suspend fun <T> dbQuery(block: suspend (database: Database) -> T): T = newSuspendedTransaction(Dispatchers.IO) {
        addLogger(StdOutSqlLogger)
        block(database)
    }

    suspend fun <T> nativeQuery(block: Transaction.() -> T): T {
        val job = CoroutineScope(Dispatchers.IO).async { transaction { block() } }
        return job.await()
    }


    private fun createHikariDataSource(url: String, driver: String, user: String, password: String) = HikariDataSource(
        HikariConfig().apply {
            driverClassName = driver
            username = user
            setPassword(password)
            jdbcUrl = url
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
    )
}