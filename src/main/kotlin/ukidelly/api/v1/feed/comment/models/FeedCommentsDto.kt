package ukidelly.api.v1.feed.comment.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.api.v1.user.models.CompactUser
import java.time.LocalDateTime


@Serializable
data class FeedCommentDto(
    val id: Int,
    val content: String,
    val creator: CompactUser,
    val childComments: List<FeedCommentDto>,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime
) {
    constructor(parentComment: FeedComment, childComments: List<FeedComment> = emptyList()) : this(
        id = parentComment.id,
        content = parentComment.content,
        creator = parentComment.creator,
        childComments = childComments.map { FeedCommentDto(it) },
        createdAt = parentComment.createdAt,
        updatedAt = parentComment.updatedAt
    )
}