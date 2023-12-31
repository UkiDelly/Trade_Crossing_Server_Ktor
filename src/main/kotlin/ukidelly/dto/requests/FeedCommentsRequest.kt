package ukidelly.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateFeedCommentRequest(
  val content: String,
  val parentComment: Int? = null
)