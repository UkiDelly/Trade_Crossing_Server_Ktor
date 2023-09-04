package ukidelly.api.v1.feed.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.systems.models.DefaultProfile
import java.time.LocalDateTime


@Serializable
data class FeedPostPreviewModel(
    val postId: Int,
    val creator: String,
    val creatorIsland: String,
    val defaultProfile: DefaultProfile,
    val content: String,
    val imageUrl: List<String>,
    val commentCount: Int,
    val likeCount: Int,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime
)
