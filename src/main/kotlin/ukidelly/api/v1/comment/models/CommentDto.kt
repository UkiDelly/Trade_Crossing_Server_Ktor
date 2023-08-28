package ukidelly.api.v1.comment.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CommentDto(
	val postId: Int,
	val commentId: Int,
	val childComments: List<CommentDto>,
	val content: String,
	val creator: String,
	val creatorIsland: String,
	@Contextual
	val createdAt: LocalDateTime,
	@Contextual
	val updatedAt: LocalDateTime
) {
	companion object {
		fun fromComment(comment: Comment, childComments: List<CommentDto>): CommentDto {
			return CommentDto(
				postId = comment.postId,
				commentId = comment.commentId,
				childComments = childComments,
				content = comment.content,
				creator = comment.creator,
				creatorIsland = comment.creatorIsland,
				createdAt = comment.createdAt,
				updatedAt = comment.updatedAt
			)
		}
	}
}
