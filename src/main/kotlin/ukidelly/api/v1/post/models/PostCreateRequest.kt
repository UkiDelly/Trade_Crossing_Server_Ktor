package ukidelly.api.v1.post.models

import kotlinx.serialization.Serializable
import ukidelly.systems.models.Currency
import ukidelly.systems.models.PostCategory


@Serializable
data class PostCreateRequest(
	val title: String,
	val content: String,
	val category: PostCategory,
	val currency: Currency,
	val price: Int?,
	val closed: Boolean = false,
)
