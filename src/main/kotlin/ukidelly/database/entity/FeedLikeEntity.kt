package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.models.user.UserEntity
import ukidelly.database.tables.FeedLikes

class FeedLikeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FeedLikeEntity>(FeedLikes)

    var user by UserEntity referencedOn FeedLikes.userId
    var feed by FeedEntity referencedOn FeedLikes.postId

    override fun toString(): String {
        return "FeedLikeEntity(id=$id, user=$user, feed=$feed)"
    }
}