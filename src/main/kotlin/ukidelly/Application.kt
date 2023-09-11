package ukidelly

import io.ktor.server.application.*
import io.ktor.server.netty.*
import ukidelly.database.DataBaseFactory
import ukidelly.modules.*
import ukidelly.systems.getServerMode
import ukidelly.systems.models.ServerMode

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

//@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val serverMode = environment.config.getServerMode()


    // DB 연결
    DataBaseFactory.init(
        databaseUrl = when (serverMode) {
            ServerMode.dev -> environment.config.property("local.url").getString()
            ServerMode.prod -> environment.config.property("supabase.url").getString()
        },
        driver = when (serverMode) {
            ServerMode.dev -> "org.mariadb.jdbc.Driver"
            ServerMode.prod -> "org.postgresql.Driver"
        },

//        databaseUrl = environment.config.property("supabase.url").getString(),
        user = when (serverMode) {
            ServerMode.dev -> environment.config.property("local.username").getString()
            ServerMode.prod -> environment.config.property("supabase.username").getString()
        },
//        user = environment.config.property("supabase.username").getString(),
//        password = environment.config.property("supabase.password").getString()
        password = when (serverMode) {
            ServerMode.dev -> environment.config.property("local.password").getString()
            ServerMode.prod -> environment.config.property("supabase.password").getString()
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
