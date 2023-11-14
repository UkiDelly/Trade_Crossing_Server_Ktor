package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable


object Images : IntIdTable("images", "id") {
    val feedId = reference("feed_id", Feeds.id)
    val url = text("url")
}