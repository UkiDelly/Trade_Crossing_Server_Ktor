package ukidelly.post.domain

import kotlinx.serialization.Serializable


@Serializable
data class PostCreateRequest(
    val title: String,

    val content: String,

    val currency: Currency,

    val price: Int?,

    val category: PostCategory,

    val island: String,

    val creator: String,

    val creatorIsland: String,

    val creatorId: String
)
