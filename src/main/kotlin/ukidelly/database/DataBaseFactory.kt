package ukidelly.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.annotation.Module


@Module
object DataBaseFactory {

    fun init(databaseUrl: String, user: String, password: String) {
        val database =
            Database.connect(createHikariDataSource(databaseUrl, "org.postgresql.Driver", user, password))
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }


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