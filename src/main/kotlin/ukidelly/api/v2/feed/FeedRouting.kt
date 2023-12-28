package ukidelly.api.v2.feed


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.dto.responses.ResponseDto
import ukidelly.modules.withAuth
import ukidelly.services.FeedService
import ukidelly.systems.models.TokenType

fun Route.feedRouting() {

    val logger = LoggerFactory.getLogger("FeedRouting")
    val feedService by inject<FeedService>()

    // 최신 자유게시판 가져오기
    get<FeedRoutes> {
        val feeds = feedService.getLatestPosts(it.page, it.size)
        call.respond(HttpStatusCode.OK, ResponseDto.Success(feeds, message = "성공"))

    }

    // 게시글 가져오기
    get<FeedRoutes.FeedId> {
        val id = it.feedId
        val feed = feedService.getFeedById(id)
        call.respond(HttpStatusCode.OK, ResponseDto.Success(feed, message = "성공"))
    }

    withAuth(TokenType.access) {

        // 새로운 게시글 생성하기
        post<FeedRoutes> { }

        // 게시글 수정하기
        put<FeedRoutes.FeedId> { }

        // 게시글 삭제하기
        delete<FeedRoutes.FeedId> { }


    }
}



