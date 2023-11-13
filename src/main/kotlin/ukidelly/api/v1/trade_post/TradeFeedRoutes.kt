package ukidelly.api.v1.trade_post

import io.ktor.resources.*


@Resource("/trade-post")
class TradeFeedRoutes {
    @Resource("latest")
    class Latest(val parent: TradeFeedRoutes = TradeFeedRoutes(), val page: Int = 1, val size: Int = 10)

    @Resource("{id}")
    class Id(val parent: TradeFeedRoutes = TradeFeedRoutes(), val feedId: Int) {
        @Resource("like")
        class Like(val parent: Id)


        @Resource("comment")
        class Comment(val parent: TradeFeedRoutes.Id) {

            @Resource("new")
            class New(val parent: Comment)

            @Resource("{id}")
            class Id(val parent: Comment, val commentId: Int) {

                @Resource("reply")
                class Reply(val parent: Id) {

                    @Resource("{id}")
                    class Id(val parent: Reply, val replyId: Int)
                }
            }

        }
    }

    @Resource("new")
    class New(val parent: TradeFeedRoutes = TradeFeedRoutes())
}