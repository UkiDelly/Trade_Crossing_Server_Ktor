package ukidelly.api.v1.comment.models//package ukidelly.post.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.database.models.comment.CommentEntity
import java.time.LocalDateTime


@Serializable
data class Comment(
	val postId: Int,
	val commentId: Int,
	val parentCommentId: Int?,
	val content: String,
	@Contextual
	val creator: String,
	val creatorIsland: String,
	@Contextual
	val createdAt: LocalDateTime,
	@Contextual
	val updatedAt: LocalDateTime,
) {

	companion object {
		fun fromEntity(entity: CommentEntity, userName: String, islandName: String): Comment {
			return Comment(
				commentId = entity.id.value,
				postId = entity.postId.value,
				content = entity.commentContent,
				parentCommentId = entity.parentCommentId?.value,
				creator = userName,
				creatorIsland = islandName,
				createdAt = entity.createdAt,
				updatedAt = entity.updatedAt,
			)
		}
	}
}
