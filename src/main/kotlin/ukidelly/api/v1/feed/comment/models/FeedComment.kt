package ukidelly.api.v1.feed.comment.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.api.v1.user.models.CompactUser
import ukidelly.database.entity.FeedCommentsEntity
import java.time.LocalDateTime


@Serializable
data class FeedComment(
    val id: Int,
    val content: String,
    val creator: CompactUser,
    val parentComment: Int?,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime
) {
    constructor(entity: FeedCommentsEntity) : this(
        id = entity.id.value,
        content = entity.content,
        creator = CompactUser(entity.user),
        parentComment = entity.parent?.value,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )
}
