package ukidelly.database.tables

import ukidelly.database.models.user.Users

object Feeds : BaseTable("Feeds", "id") {
    val content = text("content")
    val creator = reference("creator", Users.id)
}