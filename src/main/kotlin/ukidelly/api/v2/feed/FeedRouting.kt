package ukidelly.api.v2.feed


import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.dto.responses.ResponseDto
import ukidelly.modules.getUserId
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
        post<FeedRoutes> {
            val userId = call.getUserId()
            val partDatas = call.receiveMultipart().readAllParts()
            val imageFiles = partDatas.filterIsInstance<PartData.FileItem>()
            val content = partDatas.filterIsInstance<PartData.FormItem>().first { it.name == "content" }.value
            val feed = feedService.addNewFeed(userId, imageFiles, content)

            call.respond(HttpStatusCode.Created, ResponseDto.Success(feed, message = "성공"))
        }

        // 게시글 수정하기
        patch<FeedRoutes.FeedId> { feed ->
            val feedId = feed.feedId
            val (content, newImages, deleteImages) = call.receiveMultipart().readAllParts().let { partData ->
                val content =
                    partData.filterIsInstance<PartData.FormItem>().firstOrNull { it.name == "content" }?.value
                val newImages = partData.filterIsInstance<PartData.FileItem>()
                val deleteImages =
                    partData.filterIsInstance<PartData.FormItem>()
                        .first { it.name == "deleteImages" }.value.split(",")
                        .map { it.toInt() }
                Triple(content, newImages, deleteImages)
            }

            val updatedFeed = feedService.updateFeed(feedId, newImages, deleteImages, content)

            call.respond(HttpStatusCode.OK, ResponseDto.Success(updatedFeed, message = "성공"))
        }

        // 게시글 삭제하기
        delete<FeedRoutes.FeedId> { }


    }
}



