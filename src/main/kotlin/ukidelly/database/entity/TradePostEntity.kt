package ukidelly.database.models.post

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TradePostEntity(
    id: EntityID<Int>
) : IntEntity(id) {
    companion object : IntEntityClass<TradePostEntity>(TradePosts)

    var title by TradePosts.title
    var content by TradePosts.content
    val userUUID by TradePosts.userUUID
    var category by TradePosts.category
    var currency by TradePosts.currency
    var price by TradePosts.price
    var closed by TradePosts.closed
    var createdAt by TradePosts.createdAt
    var updatedAt by TradePosts.updatedAt
}