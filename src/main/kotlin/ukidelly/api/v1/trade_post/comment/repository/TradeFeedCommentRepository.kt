package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.annotation.Single
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.user.Users


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

    suspend fun findAllComments(postId: Int): List<ResultRow> {
        return dbQuery {
            TradeFeedComments.join(
                otherTable = Users,
                JoinType.LEFT,
                onColumn = TradeFeedComments.userId,
                otherColumn = Users.uuid,
            ).slice(
                TradeFeedComments.id,
                TradeFeedComments.postId,
                TradeFeedComments.parentCommentId,
                TradeFeedComments.commentContent,
                Users.id,
                Users.userName,
                Users.islandName,
                TradeFeedComments.createdAt,
                TradeFeedComments.updatedAt
            ).selectAll().toList()
        }
    }

    suspend fun addNewComment(postId: EntityID<Int>) {

        TradeFeedCommentEntity.new {
            this.postId = postId
            this.commentContent = ""
        }
    }

    suspend fun updateComment(commentId: Int, content: String) {

    }
}