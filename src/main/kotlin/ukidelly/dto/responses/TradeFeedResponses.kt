package ukidelly.dto.responses

import kotlinx.serialization.Serializable
import ukidelly.models.TradeFeedDetail
import ukidelly.models.TradeFeedPreview


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