package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import ukidelly.database.models.post.PostTable
import ukidelly.database.models.user.UserTable
import java.time.LocalDateTime

object CommentTable : IntIdTable("comment", "comment_id") {
	val postId = reference("post_id", PostTable.id)
	val parentCommentId = reference("parent_comment_id", CommentTable.id).nullable()
	val commentContent = text("comment_content")
	val userId = reference("user_id", UserTable.id, fkName = "comment_user_fk")
	val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
	val updatedAt = datetime("updated_at")
}