package ukidelly.systems

import io.ktor.server.config.*
import ukidelly.systems.models.ServerMode

fun ApplicationConfig.getServerMode(): ServerMode =
	ServerMode.valueOf(this.propertyOrNull("server.mode")?.getString() ?: "DEV")