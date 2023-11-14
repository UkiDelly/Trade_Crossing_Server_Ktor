package ukidelly.api.v1.trade_post.comment.models

import kotlinx.serialization.Serializable


@Serializable
data class NewCommentRequest(
    val content: String
)