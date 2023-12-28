package ukidelly.api.v2.`trade-feed`.comments

import io.ktor.resources.*
import ukidelly.api.v2.`trade-feed`.TradeFeedRoutes


@Resource("comments")
class TradeFeedCommentRoute(val feed: TradeFeedRoutes.FeedId) {

    @Resource("{commentId}")
    class CommentId(val comments: TradeFeedCommentRoute, val commentId: Int) {

        @Resource("reply")
        class Reply(val comment: CommentId)
    }
}