package ukidelly.api.v2.feed.comments

import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.Route
import ukidelly.modules.withAuth
import ukidelly.systems.models.TokenType


fun Route.feedCommentRoutes() {

  get<FeedCommentRoutes> {

  }

  withAuth(TokenType.access) {

    post<FeedCommentRoutes> {}

    patch<FeedCommentRoutes.CommentId> {}

    delete<FeedCommentRoutes.CommentId> {}
  }

}