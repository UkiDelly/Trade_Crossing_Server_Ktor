package ukidelly.database.models.comment

import org.jetbrains.exposed.sql.select

import ukidelly.api.v1.post.models.Comment
import ukidelly.utils.Utils


object CommentDao {

    fun entityToComment(entity: CommentEntity): Comment = Comment(
        commentId = entity.id.value,
        postId = entity.postId,
        parentCommentId = entity.parentCommentId,
        content = entity.content,
        creator = entity.creator,
        creatorIsland = entity.creatorIsland,
        createdAt = Utils.convertStringToLocalDateTime(entity.createdAt),
        updatedAt = Utils.convertStringToLocalDateTime(entity.updatedAt)
    )

    suspend fun getCommentsCountInPost(postId: Int): Int {
        return CommentTable.select { CommentTable.postId eq postId }.count().toInt()
    }
}