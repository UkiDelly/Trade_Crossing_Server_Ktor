package ukidelly.api.v1.feed

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.api.v1.feed.service.FeedService

fun Route.feedRouting() {

    val logger = LoggerFactory.getLogger("FeedRouting")
    val feedService by inject<FeedService>()

    route("/feed") {
        get("/latest") { }

        get("/{postId}") { }

        authenticate("auth-jwt") {

            post("/new") {

            }

            route("/{postId}") {

                put { }

                delete { }
            }
        }
    }
}



