package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CommentEntity(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<CommentEntity>(CommentTable)

    var postId by CommentTable.postId
    var parentCommentId by CommentTable.parentCommentId
    var commentContent by CommentTable.commentContent
    var userId by CommentTable.userId
    var createdAt by CommentTable.createdAt
    var updatedAt by CommentTable.updatedAt
}


