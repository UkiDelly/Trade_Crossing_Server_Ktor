package ukidelly.api.v2.feed


import io.github.smiley4.ktorswaggerui.dsl.resources.delete
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.github.smiley4.ktorswaggerui.dsl.resources.patch
import io.github.smiley4.ktorswaggerui.dsl.resources.post
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.api.v2.feed.comments.feedCommentRoutes
import ukidelly.dto.responses.LatestFeedDto
import ukidelly.dto.responses.ResponseDto
import ukidelly.modules.getUserId
import ukidelly.modules.withAuth
import ukidelly.services.FeedService
import ukidelly.systems.errors.ServerError
import ukidelly.systems.models.TokenType

fun Route.feedRouting() {

  val logger = LoggerFactory.getLogger("FeedRouting")
  val feedService by inject<FeedService>()

  // 최신 자유게시판 가져오기
  get<FeedRoutes>({
    description = "최신 자유게시판 가져오기"
    tags = listOf("피드")

    request {
      queryParameter<Int>("page") {
        description = "페이지 번호"
        example = 1
      }
      queryParameter<Int>("size") {
        description = "페이지 사이즈"
        example = 10
      }
    }

    response { HttpStatusCode.OK to { body<LatestFeedDto>() } }
  }) {
    val feeds = feedService.getLatestPosts(it.page, it.size)
    call.respond(HttpStatusCode.OK, ResponseDto.Success(feeds, message = "성공"))
  }

  // 게시글 가져오기
  get<FeedRoutes.FeedId>({
    description = "게시글 가져오기"
    tags = listOf("피드")

    request {
      pathParameter<Int>("feedId") {
        description = "게시글 아이디"
        example = 1
        required = true
      }
    }

    response {
      HttpStatusCode.OK to { body<LatestFeedDto>() }

      HttpStatusCode.NotFound to {
        body<ResponseDto.Error> { example("게시글 X", ResponseDto.Error(ServerError.NotExist, "게시글을 찾을수 없습니다.")) }
      }
    }
  }) {
    val id = it.feedId
    val feed = feedService.getFeedById(id)
    call.respond(HttpStatusCode.OK, ResponseDto.Success(feed, message = "성공"))
  }

  withAuth(TokenType.access) {

    // 새로운 게시글 생성하기
    post<FeedRoutes>({
      description = "새로운 게시글 생성하기"
      tags = listOf("피드", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        headerParameter<String>("multipart/form-data") {
          description = "multipart/form-data"
          example = "multipart/form-data"
        }

        body<Any> {
          example("내용만", mapOf("content" to "안녕하세요"))
          example(
            "이미지도 함께", mapOf(
              "content" to "안녕하세요",
              "images" to listOf("image1", "image2")
            )
          )
        }
      }

      response {
        HttpStatusCode.Created to { body<LatestFeedDto>() }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "토큰이 없을때",
              ResponseDto.Error(ServerError.Unauthorized, "토큰이 없습니다.")
            )
          }
        }
      }
    }) {
      val userId = call.getUserId()
      val partDatas = call.receiveMultipart().readAllParts()
      val imageFiles = partDatas.filterIsInstance<PartData.FileItem>()
      val content = partDatas.filterIsInstance<PartData.FormItem>().first { it.name == "content" }.value
      val feed = feedService.addNewFeed(userId, imageFiles, content)

      call.respond(HttpStatusCode.Created, ResponseDto.Success(feed, message = "성공"))
    }

    // 게시글 수정하기
    patch<FeedRoutes.FeedId>({
      description = "게시글 수정하기"
      tags = listOf("피드", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        headerParameter<String>("multipart/form-data") {
          description = "multipart/form-data"
          example = "multipart/form-data"
        }

        pathParameter<Int>("feedId") {
          description = "게시글 아이디"
          example = 1
          required = true
        }

        body<Any> {
          example("내용만", mapOf("content" to "안녕하세요"))
          example(
            "새로운 이미지 추가", mapOf(
              "content" to "안녕하세요",
              "images" to listOf("image1", "image2")
            )
          )
          example(
            "기존 이미지 삭제", mapOf(
              "content" to "안녕하세요",
              "deleteImages" to listOf(1, 2)
            )
          )
          example(
            "이미지 추가 및 삭제", mapOf(
              "content" to "안녕하세요",
              "images" to listOf("image1", "image2"),
              "deleteImages" to listOf(1, 2)
            )
          )
        }
      }

      response {
        HttpStatusCode.OK to { body<LatestFeedDto>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "게시글 X",
              ResponseDto.Error(ServerError.NotExist, "게시글을 찾을수 없습니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "토큰이 없을때",
              ResponseDto.Error(ServerError.Unauthorized, "토큰이 없습니다.")
            )
          }
        }
      }
    }) { feed ->
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
    delete<FeedRoutes.FeedId>({
      description = "게시글 삭제하기"
      tags = listOf("피드", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        pathParameter<Int>("feedId") {
          description = "게시글 아이디"
          example = 1
          required = true
        }
      }

      response {
        HttpStatusCode.OK to { body<Boolean>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "게시글 X",
              ResponseDto.Error(ServerError.NotExist, "게시글을 찾을수 없습니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "토큰이 없을때",
              ResponseDto.Error(ServerError.Unauthorized, "토큰이 없습니다.")
            )
          }
        }
      }
    }) {
      val feedId = it.feedId
      feedService.deleteFeed(feedId)
      call.respond(HttpStatusCode.OK, ResponseDto.Success(true, message = "성공"))
    }

  }
  feedCommentRoutes()
}



