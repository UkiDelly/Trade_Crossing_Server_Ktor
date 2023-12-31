package ukidelly.api.v2.feed.comments

import io.github.smiley4.ktorswaggerui.dsl.resources.delete
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.github.smiley4.ktorswaggerui.dsl.resources.patch
import io.github.smiley4.ktorswaggerui.dsl.resources.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.dto.requests.CreateFeedCommentRequest
import ukidelly.dto.responses.FeedCommentDto
import ukidelly.dto.responses.ResponseDto
import ukidelly.modules.getUserId
import ukidelly.modules.withAuth
import ukidelly.services.FeedCommentService
import ukidelly.systems.errors.ServerError
import ukidelly.systems.models.TokenType


fun Route.feedCommentRoutes() {
  val logger = LoggerFactory.getLogger("FeedCommentRoutes")
  val feedService by inject<FeedCommentService>()

  get<FeedCommentRoutes>({
    description = "게시글 댓글 가져오기"
    tags = listOf("피드", "댓글")

    request {
      pathParameter<Int>("feedId") {
        description = "게시글 아이디"
        example = 1
      }
    }

    response {
      HttpStatusCode.OK to { body<List<FeedCommentDto>>() }
      HttpStatusCode.NotFound to {
        body<ResponseDto.Error> {
          example(
            "게시들 X",
            ResponseDto.Error(ServerError.NotExist, "게시글이 존재하지 않습니다.")
          )
        }
      }
    }

  }) {
    val feedId = it.feed.feedId
    val comments = feedService.getFeedComments(feedId)
    call.respond(HttpStatusCode.OK, comments)
  }

  withAuth(TokenType.access) {

    post<FeedCommentRoutes>({
      description = "게시글 댓글 작성"
      tags = listOf("피드", "댓글", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        pathParameter<Int>("feedId") {
          description = "게시글 아이디"
          example = 1
        }
        body<CreateFeedCommentRequest>()
      }

      response {
        HttpStatusCode.Created to { body<ResponseDto.Success<Unit>>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "게시글 X",
              ResponseDto.Error(ServerError.NotExist, "게시글이 존재하지 않습니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "권한 X",
              ResponseDto.Error(ServerError.Unauthorized, "권한이 없습니다.")
            )
          }
        }
      }

    }) {
      val feedId = it.feed.feedId
      val userUUID = call.getUserId()
      val body = call.receive<CreateFeedCommentRequest>()
      feedService.addComment(feedId, userUUID, body)
      call.respond(HttpStatusCode.Created)
    }

    patch<FeedCommentRoutes.CommentId>({
      description = "게시글 댓글 수정"
      tags = listOf("피드", "댓글", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        pathParameter<Int>("commentId") {
          description = "댓글 아이디"
          example = 1
        }
        pathParameter<Int>("feedId") {
          description = "게시글 아이디"
          example = 1
        }
        body<CreateFeedCommentRequest>()
      }

      response {
        HttpStatusCode.OK to { body<ResponseDto.Success<Unit>>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "댓글 X",
              ResponseDto.Error(ServerError.NotExist, "댓글이 존재하지 않습니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "권한 X",
              ResponseDto.Error(ServerError.Unauthorized, "권한이 없습니다.")
            )
          }
        }
      }
    }) {
      val commentId = it.commentId
      val userUUID = call.getUserId()
      val body = call.receive<CreateFeedCommentRequest>()
      feedService.updateComment(commentId, userUUID, body)
      call.respond(HttpStatusCode.OK)
    }

    delete<FeedCommentRoutes.CommentId>({
      description = "게시글 댓글 삭제"
      tags = listOf("피드", "댓글", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        pathParameter<Int>("commentId") {
          description = "댓글 아이디"
          example = 1
        }
        pathParameter<Int>("feedId") {
          description = "게시글 아이디"
          example = 1
        }
      }

      response {
        HttpStatusCode.OK to { body<ResponseDto.Success<Unit>>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "댓글 X",
              ResponseDto.Error(ServerError.NotExist, "댓글이 존재하지 않습니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "권한 X",
              ResponseDto.Error(ServerError.Unauthorized, "권한이 없습니다.")
            )
          }
        }
      }
    }) {
      val commentId = it.commentId
      val userUUID = call.getUserId()
      feedService.deleteComment(commentId, userUUID)
      call.respond(HttpStatusCode.OK)
    }

    post<FeedCommentRoutes.CommentId.Reply>({
      description = "게시글 댓글에 답글 작성"
      tags = listOf("피드", "댓글", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        pathParameter<Int>("commentId") {
          description = "댓글 아이디"
          example = 1
        }
        pathParameter<Int>("feedId") {
          description = "게시글 아이디"
          example = 1
        }
        body<CreateFeedCommentRequest>()
      }

      response {
        HttpStatusCode.Created to { body<ResponseDto.Success<Unit>>() }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "댓글 X",
              ResponseDto.Error(ServerError.NotExist, "댓글이 존재하지 않습니다.")
            )
          }
        }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "권한 X",
              ResponseDto.Error(ServerError.Unauthorized, "권한이 없습니다.")
            )
          }
        }
      }
    }) {
      val commentId = it.comment.commentId
      val userUUID = call.getUserId()
      val body = call.receive<CreateFeedCommentRequest>()
      feedService.addComment(commentId, userUUID, body)
      call.respond(HttpStatusCode.Created)
    }
  }

}