package ukidelly.modules

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ukidelly.api.v1.chat.chatRouting
import ukidelly.api.v1.feed.feedRouting
import ukidelly.api.v1.image.imageRouting
import ukidelly.api.v1.trade_post.tradeFeedRouting
import ukidelly.api.v1.user.userRouting

fun Application.configureRouting() {
    install(Resources)
    routing {
        userRouting()
        tradeFeedRouting()
        feedRouting()
        route("/chat") {
            chatRouting()
        }
        route("/image") {
            imageRouting()
        }

        get {
            call.respondText("Hello World!")
        }
    }
}
