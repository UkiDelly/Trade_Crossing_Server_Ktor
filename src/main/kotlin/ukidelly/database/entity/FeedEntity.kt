package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.models.user.UserTable
import ukidelly.database.tables.FeedTable

class FeedEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FeedEntity>(FeedTable)

    var content by FeedTable.content
    var userId by UserTable.id
    var createdAt by FeedTable.createdAt
    var updatedAt by FeedTable.updatedAt

}