package ukidelly.systems

import io.ktor.server.config.*
import ukidelly.systems.models.ServerMode

fun ApplicationConfig.getServerMode(): ServerMode =
    ServerMode.valueOf(this.propertyOrNull("server.mode")?.getString() ?: "dev")


fun ApplicationConfig.getDBUrl(mode: ServerMode): String =
    when (mode) {
        ServerMode.dev -> "jdbc:mariadb://localhost:3306/trade_crossing"
        ServerMode.prod -> this.property("database.url").getString()
    }

fun ApplicationConfig.getDriver(mode: ServerMode) =
    when (mode) {
        ServerMode.dev -> "org.mariadb.jdbc.Driver"
        ServerMode.prod -> "org.postgresql.Driver"
    }

fun ApplicationConfig.getUser(mode: ServerMode): String =
    when (mode) {
        ServerMode.dev -> "root"
        ServerMode.prod -> this.property("database.user").getString()
    }


fun ApplicationConfig.getPassword(mode: ServerMode): String =
    when (mode) {
        ServerMode.dev -> "1234"
        ServerMode.prod -> this.property("database.password").getString()
    }