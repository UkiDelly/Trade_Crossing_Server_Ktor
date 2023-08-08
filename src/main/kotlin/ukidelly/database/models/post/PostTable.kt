package ukidelly.database.models.post

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import ukidelly.api.v1.post.models.Currency
import ukidelly.api.v1.post.models.PostCategory
import ukidelly.database.models.user.UserTable
import java.time.LocalDateTime

object PostTable : IntIdTable("post", "post_id") {
    val title = varchar("title", 255)
    val content = text("content")
    val userId = reference("user_id", UserTable.id)
    val category = enumerationByName<PostCategory>("category", 100)
    val currency = enumerationByName<Currency>("currency", 100)
    val price = integer("price").nullable()
    val closed = bool("is_closed")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at")
}
