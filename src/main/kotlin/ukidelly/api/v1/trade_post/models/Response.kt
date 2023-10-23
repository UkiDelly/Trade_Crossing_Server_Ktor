package ukidelly.api.v1.trade_post.models

import kotlinx.serialization.Serializable
import ukidelly.api.v1.trade_post.comment.models.TradePostCommentDto


@Serializable
data class LatestTradePostDto(
    val posts: List<TradePostPreview>,
    val currentPage: Int,
    val totalPages: Int
)

@Serializable
data class TradePostDetailDto(
    val post: TradePostDetail,
    val comments: List<TradePostCommentDto>
)