package ukidelly.modules

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ukidelly.api.v1.chat.chatRouting
import ukidelly.api.v1.image.imageRouting
import ukidelly.api.v1.trade_post.tradePostRouting
import ukidelly.api.v1.user.userRouting

fun Application.configureRouting() {


    routing {

        route("/user") {
            userRouting()
        }

        route("/trade-post") {
            tradePostRouting()
        }

        route("/chat") {
            chatRouting()
        }

        route("/image") {
            imageRouting()
        }

    }


}
