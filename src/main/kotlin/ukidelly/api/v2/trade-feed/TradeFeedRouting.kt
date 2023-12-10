package ukidelly.api.v2.`trade-feed`

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
import ukidelly.api.v2.`trade-feed`.comments.tradeFeedCommentRoutes
import ukidelly.dto.requests.CreateTradeFeedRequestDto
import ukidelly.dto.responses.ResponseDto
import ukidelly.services.TradeFeedService
import java.util.*

fun Route.tradeFeedRouting() {
    val tradeFeedService by inject<TradeFeedService>()


    // 최신 게시글 가져오기
    get<TradeFeedRoutes> { feed ->
        val feeds = tradeFeedService.getLatestPosts(feed.size, feed.page)
        call.respond(HttpStatusCode.OK, ResponseDto.Success(feeds, "성공"))
    }


    // 게시글 가져오기
    get<TradeFeedRoutes.FeedId> { feed ->
        
        val feedData = tradeFeedService.getPost(feed.feed_id)
        call.respond(HttpStatusCode.OK, ResponseDto.Success(feedData, "성공"))
    }

    authenticate("auth-jwt") {

        // 새 게시물
        post<TradeFeedRoutes> {
            val uuid = call.principal<UserIdPrincipal>()!!.name
            val reqeust = call.receive<CreateTradeFeedRequestDto>()
            val newPost = tradeFeedService.addNewPost(reqeust, UUID.fromString(uuid))
            call.respond(HttpStatusCode.OK, ResponseDto.Success(newPost, "성공"))
        }

        // 좋아요
        post<TradeFeedRoutes.FeedId.Like> {}

        // 수정
        put<TradeFeedRoutes.FeedId> { feed ->


        }
        // 삭제
        delete<TradeFeedRoutes.FeedId> { feed ->
            tradeFeedService.deletePost(feed.feed_id)
            call.respond(HttpStatusCode.OK, "성공")
        }
    }

    // 댓글
    tradeFeedCommentRoutes()
}