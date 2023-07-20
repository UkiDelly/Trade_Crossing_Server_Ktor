package ukidelly.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ukidelly.routing.user.userRouting

fun Application.configureRouting() {
    routing {
        
        get("/") {
            call.respondText("Hello World!")
        }

        route("/user") {
            userRouting()
        }

    }
}
