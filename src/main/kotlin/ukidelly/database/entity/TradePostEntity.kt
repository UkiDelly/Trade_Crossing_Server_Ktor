package ukidelly.database.models.post

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TradePostEntity(
    id: EntityID<Int>
) : IntEntity(id) {
    companion object : IntEntityClass<TradePostEntity>(TradePostTable)

    var title by TradePostTable.title
    var content by TradePostTable.content
    var userId by TradePostTable.userId
    var category by TradePostTable.category
    var currency by TradePostTable.currency
    var price by TradePostTable.price
    var closed by TradePostTable.closed
    var createdAt by TradePostTable.createdAt
    var updatedAt by TradePostTable.updatedAt
}