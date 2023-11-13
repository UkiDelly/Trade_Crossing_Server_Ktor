package ukidelly.api.v1.trade_post.comment

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ukidelly.api.v1.trade_post.comment.service.TradeFeedCommentService
import ukidelly.systems.models.ResponseDto

fun Route.tradePostCommentRoutes() {

    val tradeFeedCommentService by inject<TradeFeedCommentService>()

    // get all the comments
    get {
        val comments = tradeFeedCommentService.getAllComment(1)
        call.respond(HttpStatusCode.OK, ResponseDto.Success(comments, "success"))
    }


    // do with a specific comment
    route("/{commentId}") {

        put { }

        delete { }

        post("/reply") { }
    }
}