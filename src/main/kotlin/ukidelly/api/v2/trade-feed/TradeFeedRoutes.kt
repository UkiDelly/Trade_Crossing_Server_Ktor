package ukidelly.api.v2.`trade-feed`

import io.ktor.resources.*


@Resource("/trades")
class TradeFeedRoutes(val page: Int = 1, val size: Int = 10) {

    @Resource("{feedId}")
    class FeedId(val trades: TradeFeedRoutes = TradeFeedRoutes(), val feedId: Int) {
        @Resource("like")
        class Like(val feed: FeedId)
    }
}

