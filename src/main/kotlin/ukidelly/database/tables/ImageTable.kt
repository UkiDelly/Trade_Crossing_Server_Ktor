package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object ImageTable : IntIdTable("images", "id") {
    val postId = reference("post_id", FeedTable.id)
    val url = text("url")
}