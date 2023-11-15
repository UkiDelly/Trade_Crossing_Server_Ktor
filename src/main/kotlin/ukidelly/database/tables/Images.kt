package ukidelly.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable


object Images : IntIdTable("Images", "id") {
    val url = text("url")
}