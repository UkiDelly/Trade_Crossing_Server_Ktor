package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TradePostCommentEntity(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<TradePostCommentEntity>(TradePostComments)

    var postId by TradePostComments.postId
    var parentCommentId by TradePostComments.parentCommentId
    var commentContent by TradePostComments.commentContent
    var userId by TradePostComments.userId
    var createdAt by TradePostComments.createdAt
    var updatedAt by TradePostComments.updatedAt
}


