package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import ukidelly.database.models.user.UserTable

object ImageTable : IntIdTable("images", "id") {

    val url = text("url")
    val postId = reference("post_id", FeedTable.id)
    val userId = reference("user_id", UserTable.id)
}