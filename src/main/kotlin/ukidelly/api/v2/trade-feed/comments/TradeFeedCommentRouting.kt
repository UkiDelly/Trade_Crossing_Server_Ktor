package ukidelly.api.v2.`trade-feed`.comments


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
import ukidelly.dto.requests.NewCommentRequestDto
import ukidelly.dto.responses.ResponseDto
import ukidelly.services.TradeFeedCommentService
import java.util.*

fun Route.tradeFeedCommentRoutes() {

    val tradeFeedCommentService by inject<TradeFeedCommentService>()


    // 모든 댓글 조회
    get<TradeFeedCommentRoute> {

        val id = it.parent.feed_id
        val commentList = tradeFeedCommentService.getAllComment(id)
        call.respond(HttpStatusCode.OK, ResponseDto.Success(commentList, "성공"))

    }

    authenticate("auth-jwt") {
        // 댓글 추가
        post<TradeFeedCommentRoute> {
            val id = it.parent.feed_id
            val request = call.receive<NewCommentRequestDto>()
            val uuid = call.principal<UserIdPrincipal>()!!.name
            tradeFeedCommentService.addNewComment(id, request.content, UUID.fromString(uuid))
            call.respond(HttpStatusCode.OK, "성공")
        }


        // 댓글 수정
        put<TradeFeedCommentRoute.CommentId> {
            val id = it.comment_id
            val request = call.receive<NewCommentRequestDto>()
            tradeFeedCommentService.updateComment(id, request.content)
            call.respond(HttpStatusCode.OK, "성공")

        }

        // 댓글 삭제
        delete<TradeFeedCommentRoute.CommentId> {
            val id = it.comment_id
            tradeFeedCommentService.deleteComment(id)
        }


        // 대댓글 추가
        post<TradeFeedCommentRoute.CommentId.Reply> {

            val feedId = it.parent.parent.parent.feed_id
            val commentId = it.parent.comment_id
            val uuid = call.principal<UserIdPrincipal>()!!.name
            val request = call.receive<NewCommentRequestDto>()
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