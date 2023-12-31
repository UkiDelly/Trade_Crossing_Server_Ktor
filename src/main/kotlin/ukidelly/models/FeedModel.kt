package ukidelly.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.database.entity.FeedEntity
import ukidelly.database.entity.FeedImageEntity
import ukidelly.systems.models.DefaultProfile
import java.time.LocalDateTime


@Serializable
data class Feed(
    val id: Int,
    val creator: CompactUser,
    val content: String,
    val images: List<FeedImage>,
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
        images = feedEntity.images.map { FeedImage(it) },
        likes = feedEntity.likes.map { CompactUser(it.user) },
        createdAt = LocalDateTime.parse(feedEntity.createdAt.toString()),
        updatedAt = LocalDateTime.parse(feedEntity.updatedAt.toString()),
    )
}

@Serializable
data class FeedImage(
    val id: Int,
    val url: String
) {
    constructor(feedImageEntity: FeedImageEntity) : this(
        id = feedImageEntity.id.value,
        url = feedImageEntity.image.url
    )
}


@Serializable
data class FeedPreview(
    val postId: Int,
    val creator: String,
    val creatorIsland: String,
    val defaultProfile: DefaultProfile,
    val content: String,
    var imageUrl: List<FeedImage>,
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
        imageUrl = entity.images.map { FeedImage(it) },
        commentCount = entity.comments.count().toInt(),
        likes = entity.likes.map { it.user.uuid.toString() },
        createdAt = LocalDateTime.parse(entity.createdAt.toString()),
        updatedAt = LocalDateTime.parse(entity.updatedAt.toString()),
    )
}
