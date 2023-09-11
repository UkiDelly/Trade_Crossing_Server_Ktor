package ukidelly.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Module
import ukidelly.database.models.comment.TradePostCommentTable
import ukidelly.database.models.like.LikeTable
import ukidelly.database.models.post.TradePostTable
import ukidelly.database.models.user.UserTable
import ukidelly.database.tables.FeedTable
import ukidelly.database.tables.ImageTable


@Module
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
                UserTable,
                TradePostTable,
                TradePostCommentTable,
                LikeTable,
                ImageTable,
                FeedTable,
                withLogs = true
            )

        }

    }

    suspend fun <T> dbQuery(block: suspend (database: Database) -> T): T = newSuspendedTransaction(Dispatchers.IO) {
        addLogger(StdOutSqlLogger)
        block(database)
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