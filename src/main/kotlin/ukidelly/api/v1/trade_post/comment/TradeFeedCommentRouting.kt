package ukidelly.api.v1.trade_post.comment


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ukidelly.api.v1.trade_post.TradeFeedRoutes
import ukidelly.api.v1.trade_post.comment.models.NewCommentRequest
import ukidelly.api.v1.trade_post.comment.service.TradeFeedCommentService
import java.util.*

fun Route.tradeFeedCommentRoutes() {

    val tradeFeedCommentService by inject<TradeFeedCommentService>()


    // 모든 댓글 조회
    get<TradeFeedRoutes.FeedId.Comment> {

        val id = it.parent.id
        tradeFeedCommentService.getAllComment(id)
    }

    authenticate("auth-jwt") {
        // 댓글 추가
        post<TradeFeedRoutes.FeedId.Comment.New> {
            val id = it.parent.parent.id
            val request = call.receive<NewCommentRequest>()
            val uuid = call.principal<UserIdPrincipal>()!!.name
            tradeFeedCommentService.addNewComment(id, request.content, UUID.fromString(uuid))
            call.respond(HttpStatusCode.OK, "성공")
        }


        // 댓글 수정
        put<TradeFeedRoutes.FeedId.Comment.CommentId> {
            val id = it.id

        }

        // 댓글 삭제
        delete<TradeFeedRoutes.FeedId.Comment.CommentId> {
            val id = it.id
        }


        // 대댓글 추가
        post<TradeFeedRoutes.FeedId.Comment.CommentId.Reply> {

        }
    }


}