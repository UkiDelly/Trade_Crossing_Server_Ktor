package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import ukidelly.database.models.user.UserTable
import java.time.LocalDateTime

object FeedTable : IntIdTable("feed_post", "post_id") {
    val content = text("content")
    val userId = reference("user_id", UserTable.id, fkName = "feed_post_user_fk")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}