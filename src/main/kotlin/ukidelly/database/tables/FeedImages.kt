package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object FeedImages : IntIdTable("FeedImages", "id") {
    val feedId = reference("feed_id", Feeds.id)
    val imageId = reference("image_id", Images.id, onDelete = ReferenceOption.CASCADE)
}