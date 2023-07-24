package ukidelly.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ukidelly.user.routing.userRouting

fun Application.configureRouting() {


    routing {


        route("/user") {
            userRouting()
        }

        route("/post") {

        }


    }


}
