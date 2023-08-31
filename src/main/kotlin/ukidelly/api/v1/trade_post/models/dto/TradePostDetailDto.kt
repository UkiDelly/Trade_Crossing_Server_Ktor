package ukidelly.api.v1.trade_post.models.dto

import kotlinx.serialization.Serializable
import ukidelly.api.v1.trade_post.comment.models.TradePostCommentDto
import ukidelly.api.v1.trade_post.models.TradePostDetail

@Serializable
data class TradePostDetailDto(
    val post: TradePostDetail,
    val comments: List<TradePostCommentDto>
)
