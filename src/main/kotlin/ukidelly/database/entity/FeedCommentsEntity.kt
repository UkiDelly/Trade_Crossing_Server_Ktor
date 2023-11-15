package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.tables.FeedComments

class FeedCommentsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FeedCommentsEntity>(FeedComments)

    var feed by FeedEntity referencedOn FeedComments.feedId
    var content by FeedComments.content
    var parent by FeedEntity referencedOn FeedComments.id
    var userId by FeedComments.userId
    var createdAt by FeedComments.createdAt
    var updatedAt by FeedComments.updatedAt

    override fun toString(): String {
        return "FeedCommentsEntity(feed=$feed, content=$content, parent=$parent, userId=$userId, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}