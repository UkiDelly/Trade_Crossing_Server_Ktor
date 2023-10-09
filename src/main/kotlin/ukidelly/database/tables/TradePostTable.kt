package ukidelly.database.models.post

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import ukidelly.systems.models.Currency
import ukidelly.systems.models.PostCategory
import java.time.LocalDateTime

object TradePostTable : IntIdTable("TradePost", "post_id") {
    val title = varchar("title", 255)
    val content = text("content")
    val userId = integer("user_id")
    val category = enumerationByName<PostCategory>("category", 100).default(PostCategory.item)
    val currency = enumerationByName<Currency>("currency", 100).default(Currency.bell)
    val price = integer("price").nullable()
    val closed = bool("is_closed").default(false)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}


