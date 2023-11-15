package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.models.user.UserEntity
import ukidelly.database.tables.FeedComments
import ukidelly.database.tables.FeedImages
import ukidelly.database.tables.FeedLikes
import ukidelly.database.tables.Feeds

class FeedEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FeedEntity>(Feeds)

    var content by Feeds.content
    var user by UserEntity referencedOn Feeds.creator
    val comments by FeedCommentsEntity referrersOn FeedComments.feedId
    val likes by FeedLikeEntity referrersOn FeedLikes.postId
    val images by FeedImageEntity referrersOn FeedImages.feedId
    var createdAt by Feeds.createdAt
    var updatedAt by Feeds.updatedAt

    override fun toString(): String =
        "FeedEntity(id=$id, content=$content, user=$user, comments=$comments, likes=$likes, images=$images, createdAt=$createdAt, updatedAt=$updatedAt)"
}