package ukidelly.database.models.like

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LikeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LikeEntity>(LikeTable)

    var postId by LikeTable.postId
    var userId by LikeTable.userId
}
