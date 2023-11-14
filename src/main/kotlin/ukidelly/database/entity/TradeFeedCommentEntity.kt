package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.models.user.UserEntity

class TradeFeedCommentEntity(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<TradeFeedCommentEntity>(TradeFeedComments)

    var postId by TradeFeedComments.postId
    var parentCommentId by TradeFeedComments.parentCommentId
    var commentContent by TradeFeedComments.commentContent
    var user by UserEntity referencedOn TradeFeedComments.userId
    var createdAt by TradeFeedComments.createdAt
    var updatedAt by TradeFeedComments.updatedAt
}


