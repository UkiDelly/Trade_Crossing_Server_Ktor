package ukidelly.api.v1.trade_post.models.dto

import kotlinx.serialization.Serializable
import ukidelly.api.v1.trade_post.models.TradePostPreview

@Serializable
data class LatestTradePostDto(
    val posts: List<TradePostPreview>,
    val currentPage: Int,
    val totalPages: Int
)
