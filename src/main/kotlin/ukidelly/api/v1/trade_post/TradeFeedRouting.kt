package ukidelly.api.v1.trade_post

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ukidelly.api.v1.trade_post.comment.tradeFeedCommentRoutes
import ukidelly.api.v1.trade_post.service.TradeFeedService
import ukidelly.systems.errors.ServerError
import ukidelly.systems.models.ResponseDto

fun Route.tradeFeedRouting() {
    val tradeFeedService by inject<TradeFeedService>()


    // 최신 게시글 가져오기
    get<TradeFeedRoutes.Latest> { feed ->
        val feeds = tradeFeedService.getLatestPosts(feed.size, feed.page)
        call.respond(HttpStatusCode.OK, ResponseDto.Success(feeds, "성공"))
    }


    // 게시글 가져오기
    get<TradeFeedRoutes.FeedId> { feed ->
        val feedData = tradeFeedService.getPost(feed.id)
        if (feedData == null) {
            call.respond(HttpStatusCode.NotFound, ResponseDto.Error(ServerError.NotExist, "존재하지 않는 게시글입니다."))
        } else {
            call.respond(HttpStatusCode.OK, ResponseDto.Success(feedData, "성공"))
        }
    }

    authenticate("auth-jwt") {

        // 좋아요
        post<TradeFeedRoutes.FeedId.Like> {}

        // 수정
        put<TradeFeedRoutes.FeedId> { feed ->

        }
        // 삭제
        delete<TradeFeedRoutes.FeedId> { feed ->
            tradeFeedService.deletePost(feed.id)
            call.respond(HttpStatusCode.OK, "성공")
        }
    }

    // 댓글
    tradeFeedCommentRoutes()
}