package ukidelly.api.v1.feed.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.api.v1.user.models.CompactUser
import ukidelly.database.entity.FeedEntity
import ukidelly.systems.models.DefaultProfile
import java.time.LocalDateTime


@Serializable
data class FeedPreviewModel(
    val postId: Int,
    val creator: String,
    val creatorIsland: String,
    val defaultProfile: DefaultProfile,
    val content: String,
    var imageUrl: List<String>,
    var commentCount: Int,
    var likes: List<CompactUser>,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime
) {
    constructor(entity: FeedEntity) : this(
        postId = entity.id.value,
        creator = entity.user.userName,
        creatorIsland = entity.user.islandName,
        defaultProfile = entity.user.defaultProfile,
        content = entity.content,
        imageUrl = entity.images.map { it.image.url },
        commentCount = entity.comments.count().toInt(),
        likes = entity.likes.map { CompactUser(it.user) },
        createdAt = LocalDateTime.parse(entity.createdAt.toString()),
        updatedAt = LocalDateTime.parse(entity.updatedAt.toString()),
    )
}
