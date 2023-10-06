package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object ImageTable : IntIdTable("images", "id") {

    val url = text("url")
    val postId = reference("post_id", FeedTable.id)
}