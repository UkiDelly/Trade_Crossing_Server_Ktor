package ukidelly.api.v1.feed.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.api.v1.user.models.CompactUser
import ukidelly.database.entity.FeedEntity
import ukidelly.systems.models.DefaultProfile
import java.time.LocalDateTime


@Serializable
data class Feed(
    val id: Int,
    val creator: CompactUser,
    val content: String,
    val images: List<String>,
    val likes: List<CompactUser>,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime
) {
    constructor(feedEntity: FeedEntity) : this(
        id = feedEntity.id.value,
        creator = CompactUser(feedEntity.user),
        content = feedEntity.content,
        images = feedEntity.images.map { it.image.url },
        likes = feedEntity.likes.map { CompactUser(it.user) },
        createdAt = LocalDateTime.parse(feedEntity.createdAt.toString()),
        updatedAt = LocalDateTime.parse(feedEntity.updatedAt.toString()),
    )
}


@Serializable
data class FeedPreviewModel(
    val postId: Int,
    val creator: String,
    val creatorIsland: String,
    val defaultProfile: DefaultProfile,
    val content: String,
    var imageUrl: List<String>,
    var commentCount: Int,
    var likes: List<String>,
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
        likes = entity.likes.map { it.user.uuid.toString() },
        createdAt = LocalDateTime.parse(entity.createdAt.toString()),
        updatedAt = LocalDateTime.parse(entity.updatedAt.toString()),
    )
}
