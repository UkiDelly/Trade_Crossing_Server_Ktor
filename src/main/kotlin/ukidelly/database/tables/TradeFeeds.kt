package ukidelly.database.models.post

import ukidelly.database.models.user.Users
import ukidelly.database.tables.BaseTable
import ukidelly.systems.models.Currency
import ukidelly.systems.models.PostCategory

object TradeFeeds : BaseTable("TradeFeed", "id") {
    val title = varchar("title", 255)
    val content = text("content")
    val user_id = reference("user_id", Users.id)
    val category = enumerationByName<PostCategory>("category", 100).default(PostCategory.item)
    val currency = enumerationByName<Currency>("currency", 100).default(Currency.bell)
    val price = integer("price").nullable()
    val closed = bool("is_closed").default(false)
}

