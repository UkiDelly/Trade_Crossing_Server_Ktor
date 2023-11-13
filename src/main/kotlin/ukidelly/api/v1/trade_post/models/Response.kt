package ukidelly.api.v1.trade_post.models

import kotlinx.serialization.Serializable
import ukidelly.api.v1.trade_post.comment.models.TradeFeedCommentDto


@Serializable
data class LatestTradeFeedDto(
    val posts: List<TradeFeedPreview>,
    val currentPage: Int,
    val totalPages: Int
)

@Serializable
data class TradeFeedDto(
    val post: TradeFeedDetail,
    val comments: List<TradeFeedCommentDto>
)