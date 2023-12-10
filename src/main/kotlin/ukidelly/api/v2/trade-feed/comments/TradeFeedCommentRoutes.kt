package ukidelly.api.v2.`trade-feed`.comments

import io.ktor.resources.*
import ukidelly.api.v2.`trade-feed`.TradeFeedRoutes


@Resource("comment")
class TradeFeedCommentRoute(val parent: TradeFeedRoutes.FeedId) {

    @Resource("{comment_id}")
    class CommentId(val parent: TradeFeedCommentRoute, val comment_id: Int) {

        @Resource("reply")
        class Reply(val parent: CommentId)
    }
}