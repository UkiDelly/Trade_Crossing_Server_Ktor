package ukidelly.modules

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ukidelly.api.v2.chat.chatRouting
import ukidelly.api.v2.feed.feedRouting
import ukidelly.api.v2.image.imageRouting
import ukidelly.api.v2.`trade-feed`.tradeFeedRouting
import ukidelly.api.v2.user.userRouting

fun Application.configureRouting() {
    install(Resources)
    routing {
        userRouting()
        tradeFeedRouting()
        feedRouting()
        imageRouting()
        route("/chat") {
            chatRouting()
        }

        get {
            call.respondText("Hello World!")
        }
    }
}
