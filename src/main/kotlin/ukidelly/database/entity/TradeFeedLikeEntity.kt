package ukidelly.database.models.like

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TradeFeedLikeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TradeFeedLikeEntity>(TradeFeedLikes)

    var post by TradeFeedLikes.postId
    var user by TradeFeedLikes.userId
}
