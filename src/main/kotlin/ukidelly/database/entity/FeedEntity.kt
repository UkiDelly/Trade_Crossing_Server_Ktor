package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.models.user.UserEntity
import ukidelly.database.tables.FeedImages
import ukidelly.database.tables.Feeds

class FeedEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FeedEntity>(Feeds)

    var content by Feeds.content
    var user by UserEntity referencedOn Feeds.userId
    var images by ImageEntity via FeedImages
    var createdAt by Feeds.createdAt
    var updatedAt by Feeds.updatedAt
}