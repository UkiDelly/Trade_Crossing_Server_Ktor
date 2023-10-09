package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import ukidelly.database.models.post.TradePostTable
import java.time.LocalDateTime

object TradePostCommentTable : IntIdTable("TradePostComment", "id") {
    val postId = reference("post_id", TradePostTable.id)
    val parentCommentId = reference("parent_comment_id", TradePostCommentTable.id).nullable()
    val commentContent = text("comment_content")
    val userId = integer("user_id")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }