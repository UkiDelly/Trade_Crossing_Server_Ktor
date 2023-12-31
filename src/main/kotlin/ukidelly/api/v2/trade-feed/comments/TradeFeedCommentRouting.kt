package ukidelly.api.v2.`trade-feed`.comments


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
import ukidelly.dto.requests.NewCommentRequestDto
import ukidelly.dto.responses.ResponseDto
import ukidelly.dto.responses.TradeFeedCommentDto
import ukidelly.modules.getUserId
import ukidelly.modules.withAuth
import ukidelly.services.TradeFeedCommentService
import ukidelly.systems.errors.ServerError
import ukidelly.systems.models.TokenType

fun Route.tradeFeedCommentRoutes() {

  val tradeFeedCommentService by inject<TradeFeedCommentService>()

  // 모든 댓글 조회
  get<TradeFeedCommentRoute>({
    description = "모든 댓글 조회"
    tags = listOf("거래 피드", "댓글")

    request {
      pathParameter<Int>("feedId") { required = true }
    }

    response {
      HttpStatusCode.OK to { body<List<TradeFeedCommentDto>>() }
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
    val id = it.feed.feedId
    val commentList = tradeFeedCommentService.getAllComment(id)
    call.respond(HttpStatusCode.OK, ResponseDto.Success(commentList, "성공"))

  }

  withAuth(TokenType.access) {
    // 댓글 추가
    post<TradeFeedCommentRoute>({
      description = "댓글 추가"
      tags = listOf("거래 피드", "댓글", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        pathParameter<Int>("feedId") { required = true }
        body<NewCommentRequestDto>()
      }

      response {
        HttpStatusCode.OK to { body<String>() }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.Unauthorized, "로그인이 필요합니다.")
            )
          }
        }
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
      val id = it.feed.feedId
      val request = call.receive<NewCommentRequestDto>()
      val uuid = call.getUserId()
      tradeFeedCommentService.addNewComment(id, request.content, uuid)
      call.respond(HttpStatusCode.OK, "성공")
    }


    // 댓글 수정
    patch<TradeFeedCommentRoute.CommentId>({
      description = "댓글 수정"
      tags = listOf("거래 피드", "댓글", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        pathParameter<Int>("commentId") { required = true }
        body<NewCommentRequestDto>()
      }

      response {
        HttpStatusCode.OK to { body<String>() }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.Unauthorized, "로그인이 필요합니다.")
            )
          }
        }
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
      val id = it.commentId
      val request = call.receive<NewCommentRequestDto>()
      tradeFeedCommentService.updateComment(id, request.content)
      call.respond(HttpStatusCode.OK, "성공")
    }

    // 댓글 삭제
    delete<TradeFeedCommentRoute.CommentId>({
      description = "댓글 삭제"
      tags = listOf("거래 피드", "댓글", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        pathParameter<Int>("commentId") { required = true }
      }

      response {
        HttpStatusCode.OK to { body<String>() }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.Unauthorized, "본인이 작성한 댓글이 아닙니다.")
            )
          }
        }
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
      val id = it.commentId
      tradeFeedCommentService.deleteComment(id)
      call.respond(HttpStatusCode.OK, "성공")
    }


    // 대댓글 추가
    post<TradeFeedCommentRoute.CommentId.Reply>({
      description = "대댓글 추가"
      tags = listOf("거래 피드", "댓글", "인증")
      securitySchemeName = "Access"
      protected = true

      request {
        pathParameter<Int>("commentId") { required = true }
        pathParameter<Int>("feedId") { required = true }
        body<NewCommentRequestDto>()
      }

      response {
        HttpStatusCode.OK to { body<String>() }
        HttpStatusCode.Unauthorized to {
          body<ResponseDto.Error> {
            example(
              "예시",
              ResponseDto.Error(ServerError.Unauthorized, "로그인이 필요합니다.")
            )
          }
        }
        HttpStatusCode.NotFound to {
          body<ResponseDto.Error> {
            example(
              "게시글 존재 X",
              ResponseDto.Error(ServerError.NotExist, "존재하지 않는 게시글입니다.")
            )
            example(
              "댓글 존재 X",
              ResponseDto.Error(ServerError.NotExist, "존재하지 않는 댓글입니다.")
            )
          }
        }
      }
    }) {
      val feedId = it.comment.comments.feed.feedId
      val commentId = it.comment.commentId
      val uuid = call.getUserId()
      val request = call.receive<NewCommentRequestDto>()
      tradeFeedCommentService.addNewComment(
        feedId,
        request.content,
        uuid,
        reply = true,
        parentCommentId = commentId
      )
      call.respond(HttpStatusCode.OK, "성공")
    }
  }


}