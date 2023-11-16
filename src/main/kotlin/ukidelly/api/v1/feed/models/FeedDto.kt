package ukidelly.api.v1.feed.models

import kotlinx.serialization.Serializable
import ukidelly.api.v1.feed.comment.models.FeedCommentDto


@Serializable
data class FeedDto(
    val feed: Feed,
    val comments: List<FeedCommentDto>,
)


@Serializable
data class FeedPreviewDto(
    val feed: FeedPreviewModel,

    )
