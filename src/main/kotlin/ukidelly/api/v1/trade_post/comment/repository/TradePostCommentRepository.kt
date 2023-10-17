package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.annotation.Single
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.user.UserTable


@Single
class TradePostCommentRepository {

    suspend fun findAllComments(postId: Int): List<ResultRow> {
        return dbQuery {
            TradePostCommentTable.join(
                otherTable = UserTable,
                JoinType.LEFT,
                onColumn = TradePostCommentTable.userId,
                otherColumn = UserTable.uuid,
            ).slice(
                TradePostCommentTable.id,
                TradePostCommentTable.postId,
                TradePostCommentTable.parentCommentId,
                TradePostCommentTable.commentContent,
                UserTable.id,
                UserTable.userName,
                UserTable.islandName,
                TradePostCommentTable.createdAt,
                TradePostCommentTable.updatedAt
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