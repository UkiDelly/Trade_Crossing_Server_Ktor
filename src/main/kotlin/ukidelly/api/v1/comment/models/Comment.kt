package ukidelly.api.v1.comment.models//package ukidelly.post.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import ukidelly.database.models.comment.CommentEntity
import ukidelly.database.models.comment.CommentTable
import ukidelly.database.models.user.UserTable
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

		fun fromRow(row: ResultRow): Comment {
			return Comment(
				commentId = row[CommentTable.id].value,
				postId = row[CommentTable.postId].value,
				content = row[CommentTable.commentContent],
				parentCommentId = row[CommentTable.parentCommentId]?.value,
				creator = row[UserTable.userName],
				creatorIsland = row[UserTable.islandName],
				createdAt = LocalDateTime.parse(row[CommentTable.createdAt].toString()),
				updatedAt = LocalDateTime.parse(row[CommentTable.updatedAt].toString()),
			)
		}
	}
}
