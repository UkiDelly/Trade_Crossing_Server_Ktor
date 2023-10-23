package ukidelly.api.v1.trade_post.models

import kotlinx.serialization.Serializable


@Serializable
data class LatestTradePostDto(
    val posts: List<TradePostPreview>,
    val currentPage: Int,
    val totalPages: Int
)