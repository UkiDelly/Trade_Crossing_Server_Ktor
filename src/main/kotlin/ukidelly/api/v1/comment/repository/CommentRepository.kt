package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.select


object CommentRepository {

//    fun entityToComment(entity: CommentEntity): Comment = Comment(
//        commentId = entity.id.value,
//        postId = entity.postId.value,
//        parentCommentId = entity.parentCommentId,
//        content = entity.commentContent,
//        creator = "",
//        creatorIsland = "",
////        creator = entity.creator,
////        creatorIsland = entity.creatorIsland,
//        createdAt = entity.createdAt,
//        updatedAt = entity.updatedAt
//    )

    suspend fun getCommentsCountInPost(postId: Int): Int {
        return CommentTable.select { CommentTable.postId eq postId }.count().toInt()
    }


    suspend fun addNewComment(postId: EntityID<Int>) {

        CommentEntity.new {
            this.postId = postId
            this.commentContent = ""
        }
    }
}