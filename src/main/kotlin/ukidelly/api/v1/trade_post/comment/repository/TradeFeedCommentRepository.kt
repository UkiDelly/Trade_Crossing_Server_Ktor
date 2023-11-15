package ukidelly.database.models.comment

import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.insertAndGetId
import org.koin.core.annotation.Single
import ukidelly.api.v1.trade_post.comment.models.TradeFeedComment
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
import java.time.LocalDateTime
import java.util.*


@Single
class TradeFeedCommentRepository {

    suspend fun findAllComments(postId: Int): List<TradeFeedComment> {

        val result = dbQuery {
            TradeFeedCommentEntity.find { TradeFeedComments.postId eq postId }.map {
                TradeFeedComment(it)
            }
        }

        return result
    }

    suspend fun addNewComment(postId: Int, content: String, uuid: UUID, reply: Boolean = false, parentCommentId: Int?) {
        dbQuery {
            val feed = TradeFeedEntity.findById(postId) ?: throw NotFoundException()
            val user = UserEntity.find { Users.uuid eq uuid }.firstOrNull() ?: throw NotFoundException("유저가 존재하지 않습니다.")
            val comment = TradeFeedComments.insertAndGetId {
                it[this.postId] = postId
                it[this.commentContent] = content
                it[this.userId] = user.id
                it[this.parentCommentId] = if (reply) {
                    val parentComment =
                        TradeFeedCommentEntity.findById(parentCommentId!!) ?: throw NotFoundException("게시글이 존재하지 않습니다.")
                    parentComment.id
                } else {
                    null
                }
            }
        }
    }

    suspend fun updateComment(commentId: Int, content: String) {
        dbQuery {
            val comment = TradeFeedCommentEntity.findById(commentId) ?: throw NotFoundException("댓글이 존재하지 않습니다.")
            comment.commentContent = content
            comment.updatedAt = LocalDateTime.now()
        }
    }

    suspend fun deleteComment(commentId: Int) {
        dbQuery {
            val comment = TradeFeedCommentEntity.findById(commentId) ?: throw NotFoundException("댓글이 존재하지 않습니다.")
            comment.delete()
        }
    }
}