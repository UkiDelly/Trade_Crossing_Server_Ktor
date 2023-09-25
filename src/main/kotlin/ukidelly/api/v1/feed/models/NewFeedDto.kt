package ukidelly.api.v1.feed.models

import kotlinx.serialization.Serializable


@Serializable
data class NewFeedDto(
    val content: String,
    val images: List<String>
)
