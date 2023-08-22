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
			ServerMode.dev -> environment.config.property("aws.dev_url").getString()
			ServerMode.prod -> environment.config.property("aws.prod_url").getString()
		},
		user = environment.config.property("aws.user").getString(),
		password = environment.config.property("aws.password").getString()
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
}
