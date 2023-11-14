package ukidelly.database.models.comment

import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.insertAndGetId
import org.koin.core.annotation.Single
import ukidelly.api.v1.trade_post.comment.models.TradeFeedComment
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
import java.util.*


@Single
class TradeFeedCommentRepository {


    suspend fun getCommentCount(postIdList: List<Int>): List<Long> {
        val result = dbQuery {
            val counts = mutableListOf<Long>()
            for (postId in postIdList) {
                val count = TradeFeedCommentEntity.find { TradeFeedComments.postId eq postId }.count()
                counts.add(count)
            }
            counts
        }
        return result
    }

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
            val user = UserEntity.find { Users.uuid eq uuid }.firstOrNull() ?: throw NotFoundException()
            val comment = TradeFeedComments.insertAndGetId {
                it[this.postId] = postId
                it[this.commentContent] = content
                it[this.userId] = user.id
                it[this.parentCommentId] = if (reply) {
                    val parentComment = TradeFeedCommentEntity.findById(parentCommentId!!) ?: throw NotFoundException()
                    parentComment.id
                } else {
                    null
                }
            }
        }
    }

    suspend fun updateComment(commentId: Int, content: String) {

    }
}