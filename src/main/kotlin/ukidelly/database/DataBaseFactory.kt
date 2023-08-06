package ukidelly.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Module
import ukidelly.database.models.post.PostTable
import ukidelly.database.models.user.UserTable


@Module
object DataBaseFactory {

    lateinit var database: Database

    fun init(databaseUrl: String, user: String, password: String) {
        database =
            Database.connect(createHikariDataSource(databaseUrl, "org.postgresql.Driver", user, password))

        transaction(database) {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(PostTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend (database: Database) -> T): T = newSuspendedTransaction(Dispatchers.IO) {
        block(
            database
        )
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