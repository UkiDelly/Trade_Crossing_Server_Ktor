package ukidelly.api.v1.post.models

import kotlinx.serialization.Serializable


@Serializable
data class PostCreateRequest(
    val title: String,
    val content: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val closed: Boolean = false,
)
