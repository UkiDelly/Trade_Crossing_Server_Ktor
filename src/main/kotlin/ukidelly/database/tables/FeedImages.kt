package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object FeedImages : IntIdTable("feed_images", "id") {
    val postId = reference("post_id", Feeds)
    val url = text("url")
}