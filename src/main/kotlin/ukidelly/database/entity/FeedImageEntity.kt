package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.tables.FeedImages

class FeedImageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FeedImageEntity>(FeedImages)

    var post by FeedEntity referencedOn FeedImages.feedId
    var image by ImageEntity referencedOn FeedImages.imageId
}