package ukidelly.api.v1.post.models.dto

import kotlinx.serialization.Serializable
import ukidelly.api.v1.comment.models.CommentDto
import ukidelly.api.v1.post.models.PostDetail

@Serializable
data class PostDetailDto(
    val post: PostDetail,
    val comments: List<CommentDto>
)
