package ukidelly.dto.responses

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.models.CompactUser
import ukidelly.models.FeedComment
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