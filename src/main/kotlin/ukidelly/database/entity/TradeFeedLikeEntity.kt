package ukidelly.database.models.like

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users

class TradeFeedLikeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TradeFeedLikeEntity>(TradeFeedLikes)

    var post by TradeFeedEntity referencedOn TradeFeedLikes.postId
    var user by UserEntity referencedOn Users.id
}
