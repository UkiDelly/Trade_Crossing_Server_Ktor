package ukidelly.api.v1.post.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class PostDto(
    val title: String,
    val content: String,
    val creator: String,
    val creatorIsland: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val closed: Boolean,
)

@Serializable
data class PostDetail(
    val postId: Int,
    val title: String,
    val content: String,
    val creator: String,
    val creatorIsland: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int,
    val closed: Boolean,
    val commentList: List<Comment>,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime,
)


@Serializable
data class PostPreview(
    val postId: Int,
    val title: String,
    val content: String,
    val creator: String,
    val creatorIsland: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val closed: Boolean,
    val commentCount: Int,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime,
)
