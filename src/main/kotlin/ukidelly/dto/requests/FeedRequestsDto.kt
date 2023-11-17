package ukidelly.dto.requests

import kotlinx.serialization.Serializable


@Serializable
data class NewFeedReqeustDto(
    val content: String,
    val images: List<String>
)
