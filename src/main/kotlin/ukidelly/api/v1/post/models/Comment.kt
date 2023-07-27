package ukidelly.api.v1.post.models//package ukidelly.post.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class Comment(
    val postId: Int,
    val commentId: Int,
    val parentCommentId: Int?,
    val content: String,
    @Contextual
    val creator: String,
    val creatorIsland: String,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime,
)
