package ukidelly.api.v1.trade_post

import io.ktor.resources.*


@Resource("/trade-post")
class TradePostRoutes {


    @Resource("latest")
    class Latest(val parent: TradePostRoutes = TradePostRoutes(), val page: Int = 1, val size: Int = 10)

    @Resource("{id}")
    class Id(val parent: TradePostRoutes = TradePostRoutes(), val id: Int)


}