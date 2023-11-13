package ukidelly.database.tables

import ukidelly.database.models.user.Users

object Feeds : BaseTable("FeedPost", "id") {
    val content = text("content")
    val userId = integer("user_id")
    val creator = reference("creator", Users.id)
}