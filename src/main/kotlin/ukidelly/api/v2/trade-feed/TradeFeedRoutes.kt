package ukidelly.api.v2.`trade-feed`

import io.ktor.resources.*


@Resource("/trade-feed")
class TradeFeedRoutes(val page: Int = 1, val size: Int = 10) {

    @Resource("{feed_id}")
    class FeedId(val parent: TradeFeedRoutes = TradeFeedRoutes(), val feed_id: Int) {
        @Resource("like")
        class Like(val parent: FeedId)
    }
}

