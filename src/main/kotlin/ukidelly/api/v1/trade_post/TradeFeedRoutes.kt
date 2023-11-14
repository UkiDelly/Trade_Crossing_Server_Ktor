package ukidelly.api.v1.trade_post

import io.ktor.resources.*


@Resource("/trade-feed")
class TradeFeedRoutes {
    @Resource("latest")
    class Latest(val parent: TradeFeedRoutes = TradeFeedRoutes(), val page: Int = 1, val size: Int = 10)

    @Resource("new")
    class New(val parent: TradeFeedRoutes = TradeFeedRoutes())

    @Resource("{feed_id}")
    class FeedId(val parent: TradeFeedRoutes = TradeFeedRoutes(), val feed_id: Int) {
        @Resource("like")
        class Like(val parent: FeedId)

        @Resource("comment")
        class Comment(val parent: FeedId) {

            @Resource("new")
            class New(val parent: Comment)

            @Resource("{comment_id}")
            class CommentId(val parent: Comment, val comment_id: Int) {

                @Resource("reply")
                class Reply(val parent: CommentId)
            }
        }
    }
}