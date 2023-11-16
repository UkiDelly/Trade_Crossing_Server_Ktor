package ukidelly

import io.ktor.server.application.*
import io.ktor.server.netty.*
import ukidelly.database.DataBaseFactory
import ukidelly.modules.*
import ukidelly.systems.getServerMode
import ukidelly.systems.models.ServerMode

fun main(args: Array<String>): Unit =
    EngineMain.main(args)


fun Application.module() {

    val serverMode = environment.config.getServerMode()

    // DB 연결
    DataBaseFactory.init(
        databaseUrl = when (serverMode) {
            ServerMode.dev -> "jdbc:mariadb://localhost:3306/trade_crossing"
            ServerMode.prod -> environment.config.property("database.url").getString()
        },
        driver = when (serverMode) {
            ServerMode.dev -> "org.mariadb.jdbc.Driver"
            ServerMode.prod -> "org.postgresql.Driver"
        },

        user = when (serverMode) {
            ServerMode.dev -> "root"
            ServerMode.prod -> environment.config.property("supabase.username").getString()
        },

        password = when (serverMode) {
            ServerMode.dev -> "1234"
            ServerMode.prod -> environment.config.property("database.password").getString()
        }
    )

    // Koin
    configureKoin()

    // Error 처리
    configureStatusPage()

    // JWT
    configureJWT()

    // swagger
    configureSwaggerUI()

    // Json
    configureSerialization()

    // Request Validation
    configureRequestValidation()

    // Log
    configureMonitoring()

    // Routing
    configureRouting()

    // Websocket
    configureWebSocket()
}
