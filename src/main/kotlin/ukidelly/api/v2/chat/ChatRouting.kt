package ukidelly.api.v2.chat

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun Route.chatRouting() {

    authenticate("auth-jwt") {
        post("/new") {

        }
    }

}