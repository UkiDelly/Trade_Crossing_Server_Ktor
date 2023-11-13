package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.tables.FeedComments

class FeedCommentsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FeedCommentsEntity>(FeedComments)

    var content by FeedComments.content
    var parentId by FeedComments.parentId
    var userId by FeedComments.userId
    var createdAt by FeedComments.createdAt
    var updatedAt by FeedComments.updatedAt
}