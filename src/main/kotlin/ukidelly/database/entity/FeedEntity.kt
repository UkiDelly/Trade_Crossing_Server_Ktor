package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.models.user.Users
import ukidelly.database.tables.Feeds

class FeedEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FeedEntity>(Feeds)

    var content by Feeds.content
    var userId by Users.id
    var createdAt by Feeds.createdAt
    var updatedAt by Feeds.updatedAt

}