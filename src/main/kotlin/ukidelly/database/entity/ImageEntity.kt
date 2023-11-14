package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.tables.Images

class ImageEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<ImageEntity>(Images)

    var feedId by Images.feedId
    var url by Images.url
}