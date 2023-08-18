package ukidelly.api.v1.post.models.dto

import kotlinx.serialization.Serializable
import ukidelly.api.v1.post.models.PostPreview

@Serializable
data class LatestPostDto(
	val posts: List<PostPreview>,
	val currentPage: Int,
	val totalPages: Int
)
