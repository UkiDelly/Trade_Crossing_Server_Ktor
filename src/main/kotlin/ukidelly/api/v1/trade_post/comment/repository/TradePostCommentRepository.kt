package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.annotation.Single
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.user.Users


@Single
class TradePostCommentRepository {

    suspend fun findAllComments(postId: Int): List<ResultRow> {
        return dbQuery {
            TradePostComments.join(
                otherTable = Users,
                JoinType.LEFT,
                onColumn = TradePostComments.userId,
                otherColumn = Users.uuid,
            ).slice(
                TradePostComments.id,
                TradePostComments.postId,
                TradePostComments.parentCommentId,
                TradePostComments.commentContent,
                Users.id,
                Users.userName,
                Users.islandName,
                TradePostComments.createdAt,
                TradePostComments.updatedAt
            ).selectAll().toList()
        }
    }

    suspend fun addNewComment(postId: EntityID<Int>) {

        TradePostCommentEntity.new {
            this.postId = postId
            this.commentContent = ""
        }
    }
}