package ukidelly

import io.ktor.server.application.*
import io.ktor.server.netty.*
import ukidelly.database.DataBaseFactory
import ukidelly.modules.*

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

//@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {


    // DB 연결
    DataBaseFactory.init(
        environment.config.property("supabase.url").getString(),
        environment.config.property("supabase.username").getString(),
        environment.config.property("supabase.password").getString()
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

    // Log
    configureMonitoring()

    // Routing
    configureRouting()
}
