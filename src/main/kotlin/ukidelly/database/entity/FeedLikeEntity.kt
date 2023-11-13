package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.tables.FeedLikes

class FeedLikeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FeedLikeEntity>(FeedLikes)

    var userId = FeedLikes.userId
    var postId = FeedLikes.postId
}