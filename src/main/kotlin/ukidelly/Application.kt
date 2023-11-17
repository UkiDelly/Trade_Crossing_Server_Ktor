package ukidelly

import io.ktor.server.application.*
import io.ktor.server.netty.*
import ukidelly.database.DataBaseFactory
import ukidelly.modules.*
import ukidelly.systems.*

fun main(args: Array<String>): Unit =
    EngineMain.main(args)


fun Application.module() {
    val serverMode = environment.config.getServerMode()

    // DB 연결
    DataBaseFactory.init(
        databaseUrl = environment.config.getDBUrl(serverMode),
        driver = environment.config.getDriver(serverMode),
        user = environment.config.getUser(serverMode),
        password = environment.config.getPassword(serverMode)
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
