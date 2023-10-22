package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import ukidelly.database.models.user.Users
import java.time.LocalDateTime

object FeedComments : IntIdTable("feed_comment", "comment_id") {
    val content = text("content")
    val parentId = reference("parent_id", FeedComments.id).nullable()
    val userId = reference("user_id", Users.id, fkName = "feed_comment_fk")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at")
}