package ukidelly.dto.requests

import kotlinx.serialization.Serializable


@Serializable
data class NewCommentRequestDto(
    val content: String
)