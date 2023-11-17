package ukidelly.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
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
