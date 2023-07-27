package ukidelly.modules

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ukidelly.api.v1.post.postRouting
import ukidelly.api.v1.user.userRouting

fun Application.configureRouting() {


    routing {


        route("/user") {
            userRouting()
        }

        route("/post") {
            postRouting()
        }


    }


}
