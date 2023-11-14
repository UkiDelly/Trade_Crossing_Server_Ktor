package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object FeedImages : IntIdTable("FeedImages", "id") {
    val feedId = reference("feed_id", Feeds.id)
    val imageId = reference("image_id", Images.id)
}