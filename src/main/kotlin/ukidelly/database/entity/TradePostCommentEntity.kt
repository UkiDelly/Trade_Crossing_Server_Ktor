package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TradePostCommentEntity(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<TradePostCommentEntity>(TradePostCommentTable)

    var postId by TradePostCommentTable.postId
    var parentCommentId by TradePostCommentTable.parentCommentId
    var commentContent by TradePostCommentTable.commentContent
    var userId by TradePostCommentTable.userId
    var createdAt by TradePostCommentTable.createdAt
    var updatedAt by TradePostCommentTable.updatedAt
}


