package ukidelly.api.v2.feed.comments

import io.ktor.resources.*
import ukidelly.api.v2.feed.FeedRoutes


@Resource("comments")
class FeedCommentRoutes(
  val feed: FeedRoutes.FeedId,
) {

  @Resource("{commentId}")
  class CommentId(val commets: FeedCommentRoutes, val commentId: String) {

    @Resource("reply")
    class Reply(val comment: CommentId)
  }
}