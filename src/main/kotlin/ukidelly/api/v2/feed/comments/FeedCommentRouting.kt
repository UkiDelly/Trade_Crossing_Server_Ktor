package ukidelly.api.v2.feed.comments

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.dto.requests.CreateFeedCommentRequest
import ukidelly.modules.getUserId
import ukidelly.modules.withAuth
import ukidelly.services.FeedCommentService
import ukidelly.systems.models.TokenType


fun Route.feedCommentRoutes() {
  val logger = LoggerFactory.getLogger("FeedCommentRoutes")
  val feedService by inject<FeedCommentService>()

  get<FeedCommentRoutes> {
    val feedId = it.feed.feedId
    val comments = feedService.getFeedComments(feedId)
    call.respond(HttpStatusCode.OK, comments)
  }

  withAuth(TokenType.access) {

    post<FeedCommentRoutes> {
      val feedId = it.feed.feedId
      val userUUID = call.getUserId()
      val body = call.receive<CreateFeedCommentRequest>()
      feedService.addComment(feedId, userUUID, body)
      call.respond(HttpStatusCode.Created)
    }

    patch<FeedCommentRoutes.CommentId> {
      val commentId = it.commentId
      val userUUID = call.getUserId()
      val body = call.receive<CreateFeedCommentRequest>()
      feedService.updateComment(commentId, userUUID, body)
      call.respond(HttpStatusCode.OK)
    }

    delete<FeedCommentRoutes.CommentId> {
      val commentId = it.commentId
      val userUUID = call.getUserId()
      val body = call.receive<CreateFeedCommentRequest>()
      feedService.updateComment(commentId, userUUID, body)
      call.respond(HttpStatusCode.OK)
    }
  }

}