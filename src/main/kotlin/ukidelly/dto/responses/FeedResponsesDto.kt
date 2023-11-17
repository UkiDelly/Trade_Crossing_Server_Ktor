package ukidelly.dto.responses

import kotlinx.serialization.Serializable
import ukidelly.models.Feed
import ukidelly.models.FeedPreview


@Serializable
data class FeedDto(
    val feed: Feed,
    val comments: List<FeedCommentDto>,
)


@Serializable
data class FeedPreviewDto(
    val feed: FeedPreview,

    )
