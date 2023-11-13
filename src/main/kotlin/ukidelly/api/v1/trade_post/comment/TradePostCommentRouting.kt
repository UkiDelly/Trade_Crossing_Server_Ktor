package ukidelly.api.v1.trade_post.comment

import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.routing.*


import org.koin.ktor.ext.inject
import ukidelly.api.v1.trade_post.TradeFeedRoutes
import ukidelly.api.v1.trade_post.comment.service.TradeFeedCommentService

fun Route.tradeFeedCommentRoutes() {

    val tradeFeedCommentService by inject<TradeFeedCommentService>()


    // 모든 댓글 조회
    get<TradeFeedRoutes.Id.Comment> {

        val id = it.parent.feedId
        tradeFeedCommentService.getAllComment(id)
    }

    // 댓글 추가
    post<TradeFeedRoutes.Id.Comment.New> {

    }


    // 댓글 수정
    put<TradeFeedRoutes.Id.Comment.Id> {
        val id = it.commentId

    }

    // 댓글 삭제
    delete<TradeFeedRoutes.Id.Comment.Id> {
        val id = it.commentId
    }


    // 대댓글 추가
    post<TradeFeedRoutes.Id.Comment.Id.Reply> {

    }

    // 대댓글 수정
    put<TradeFeedRoutes.Id.Comment.Id.Reply.Id> {
        val id = it.replyId

    }

    // 대댓글 삭제
    delete<TradeFeedRoutes.Id.Comment.Id.Reply.Id> {
        val id = it.replyId

    }


}