package ukidelly.dto.responses

import kotlinx.serialization.Serializable
import ukidelly.models.FeedPreview


@Serializable
data class LatestFeedDto(
    val page: Int,
    val size: Int,
    val totalPage: Int,
    val feeds: List<FeedPreview>
)