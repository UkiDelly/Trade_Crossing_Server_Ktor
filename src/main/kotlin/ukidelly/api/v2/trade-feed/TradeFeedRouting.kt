package ukidelly.api.v2.`trade-feed`

import io.github.smiley4.ktorswaggerui.dsl.resources.delete
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.github.smiley4.ktorswaggerui.dsl.resources.patch
import io.github.smiley4.ktorswaggerui.dsl.resources.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ukidelly.api.v2.`trade-feed`.comments.tradeFeedCommentRoutes
import ukidelly.dto.requests.CreateTradeFeedRequestDto
import ukidelly.dto.requests.UpdateTradeFeedRequestDto
import ukidelly.dto.responses.LatestFeedDto
import ukidelly.dto.responses.ResponseDto
import ukidelly.models.TradeFeedDetail
import ukidelly.modules.getUserId
import ukidelly.modules.withAuth
import ukidelly.services.TradeFeedService
import ukidelly.systems.errors.ServerError
import ukidelly.systems.models.TokenType
import java.util.*

fun Route.tradeFeedRouting() {
  val tradeFeedService by inject<TradeFeedService>()


  // 최신 게시글 가져오기
  get<TradeFeedRoutes>({
    description = "최신 게시글 가져오기"
    tags = listOf("거래 피드")
    request {
      queryParameter<Int>("page") {
        description = "페이지"
        example = 1
        allowEmptyValue = true
      }
      queryParameter<Int>("size") {
        description = "개수"
        example = 10
        allowEmptyValue = true
      }
    }

    response {
      HttpStatusCode.OK to { body<LatestFeedDto>() }
    }
  }) {
    val feeds = tradeFeedService.getLatestPosts(it.size, it.page)
    call.respond(HttpStatusCode.OK, ResponseDto.Success(feeds, "성공"))
  }


  // 게시글 가져오기
  get<TradeFeedRoutes.FeedId>({
    description = "게시글 가져오기"
    tags = listOf("거래 피드")

    request {
      pathParameter<UUID>("feedId") {
        description = "게시글 아이디"
        example = 1
        required = true
      }
    }

    response {
      HttpStatusCode.OK to { body<TradeFeedDetail>() }
      HttpStatusCode.NotFound to {
        body<ResponseDto.Error> {
          example(
            "예시",
            ResponseDto.Error(ServerError.NotExist, "존재하지 않는 게시글입니다.")
          )
        }
      }
    }
  }) {
    val feedData = tradeFeedService.getPost(it.feedId)
    call.respond(HttpStatusCode.OK, ResponseDto.Success(feedData, "성공"))
  }

  withAuth(TokenType.access) {

    // 새 게시물
    post<TradeFeedRoutes>({
      securitySchemeName = "Access"
      description = "새 게시물 생성"
      tags = listOf("거래 피드", "인증")
      request { body<CreateTradeFeedRequestDto>() }
      response {
        HttpStatusCode.Created to { body<TradeFeedDetail>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.NotExist, "존재하지 않는 게시글입니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.UnAuhtorized, "인증이 필요합니다.")
            )
          }
        }
      }
    }) {
      val uuid = call.principal<UserIdPrincipal>()!!.name
      val request = call.receive<CreateTradeFeedRequestDto>()
      val newPost = tradeFeedService.addNewPost(request, UUID.fromString(uuid))
      call.respond(HttpStatusCode.Created, ResponseDto.Success(newPost, "성공"))
    }

    // 좋아요
    post<TradeFeedRoutes.FeedId.Like>({
      securitySchemeName = "Access"
      description = "좋아요"
      tags = listOf("거래 피드", "인증")
      request {
        pathParameter<UUID>("feedId") {
          description = "게시글 아이디"
          example = 1
          required = true
        }
      }
      response {
        HttpStatusCode.OK to { body<String>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.NotExist, "존재하지 않는 게시글입니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.UnAuhtorized, "인증이 필요합니다.")
            )
          }
        }
      }
    }) {
      val feedId = it.feed.feedId
      val userId = call.getUserId()
      val result = tradeFeedService.likeFeed(feedId, userId)
      call.respond(HttpStatusCode.OK, "성공")
    }

    // 수정
    patch<TradeFeedRoutes.FeedId>({
      securitySchemeName = "Access"
      description = "게시글 수정"
      tags = listOf("거래 피드", "인증")
      request {
        pathParameter<UUID>("feedId") {
          description = "게시글 아이디"
          example = 1
          required = true
        }
        body<UpdateTradeFeedRequestDto>()
      }
      response {
        HttpStatusCode.OK to { body<TradeFeedDetail>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.NotExist, "존재하지 않는 게시글입니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.UnAuhtorized, "인증이 필요합니다.")
            )
          }
        }
      }
    }) {
      val feedId = it.feedId
      val request = call.receive<UpdateTradeFeedRequestDto>()
      val result = tradeFeedService.updateFeed(feedId, request)
      call.respond(HttpStatusCode.OK, ResponseDto.Success(result, "성공"))

    }
    // 삭제
    delete<TradeFeedRoutes.FeedId>({
      securitySchemeName = "Access"
      description = "게시글 삭제"
      tags = listOf("거래 피드", "인증")
      request {
        pathParameter<UUID>("feedId") {
          description = "게시글 아이디"
          example = 1
          required = true
        }
      }
      response {
        HttpStatusCode.OK to { body<String>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.NotExist, "존재하지 않는 게시글입니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.UnAuhtorized, "인증이 필요합니다.")
            )
          }
        }
      }
    }) {
      tradeFeedService.deletePost(it.feedId)
      call.respond(HttpStatusCode.OK, "성공")
    }
  }

  // 댓글
  tradeFeedCommentRoutes()
}