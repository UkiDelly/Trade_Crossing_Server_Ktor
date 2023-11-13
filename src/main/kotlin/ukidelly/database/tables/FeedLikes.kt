package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import ukidelly.database.models.user.Users

object FeedLikes : IntIdTable("FeedLikes", "id") {
    val userId = reference("user_id", Users.id)
    val postId = reference("post_id", Feeds.id)
}