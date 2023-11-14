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
import ukidelly.systems.models.ResponseDto
import java.util.*

fun Route.tradeFeedCommentRoutes() {

    val tradeFeedCommentService by inject<TradeFeedCommentService>()


    // 모든 댓글 조회
    get<TradeFeedRoutes.FeedId.Comment> {

        val id = it.parent.feed_id
        val commentList = tradeFeedCommentService.getAllComment(id)
        call.respond(HttpStatusCode.OK, ResponseDto.Success(commentList, "성공"))

    }

    authenticate("auth-jwt") {
        // 댓글 추가
        post<TradeFeedRoutes.FeedId.Comment.New> {
            val id = it.parent.parent.feed_id
            val request = call.receive<NewCommentRequest>()
            val uuid = call.principal<UserIdPrincipal>()!!.name
            tradeFeedCommentService.addNewComment(id, request.content, UUID.fromString(uuid))
            call.respond(HttpStatusCode.OK, "성공")
        }


        // 댓글 수정
        put<TradeFeedRoutes.FeedId.Comment.CommentId> {
            val id = it.comment_id

        }

        // 댓글 삭제
        delete<TradeFeedRoutes.FeedId.Comment.CommentId> {
            val id = it.comment_id
        }


        // 대댓글 추가
        post<TradeFeedRoutes.FeedId.Comment.CommentId.Reply> {

            val feedId = it.parent.parent.parent.feed_id
            val commentId = it.parent.comment_id
            val uuid = call.principal<UserIdPrincipal>()!!.name
            val request = call.receive<NewCommentRequest>()
            tradeFeedCommentService.addNewComment(
                feedId,
                request.content,
                UUID.fromString(uuid),
                reply = true,
                parentCommentId = commentId
            )
            call.respond(HttpStatusCode.OK, "성공")
        }
    }


}