package ukidelly.api.v1.trade_post.comment.models//package ukidelly.post.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import ukidelly.database.models.comment.TradePostCommentEntity
import ukidelly.database.models.comment.TradePostCommentTable
import ukidelly.database.models.user.UserTable
import java.time.LocalDateTime


@Serializable
data class TradePostComment(
    val postId: Int,
    val commentId: Int,
    val parentCommentId: Int?,
    val content: String,
    @Contextual
    val creator: String,
    val creatorIsland: String,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime,
) {

    companion object {
        fun fromEntity(entity: TradePostCommentEntity, userName: String, islandName: String): TradePostComment {
            return TradePostComment(
                commentId = entity.id.value,
                postId = entity.postId.value,
                content = entity.commentContent,
                parentCommentId = entity.parentCommentId?.value,
                creator = userName,
                creatorIsland = islandName,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }

        fun fromRow(row: ResultRow): TradePostComment {
            return TradePostComment(
                commentId = row[TradePostCommentTable.id].value,
                postId = row[TradePostCommentTable.postId].value,
                content = row[TradePostCommentTable.commentContent],
                parentCommentId = row[TradePostCommentTable.parentCommentId]?.value,
                creator = row[UserTable.userName],
                creatorIsland = row[UserTable.islandName],
                createdAt = LocalDateTime.parse(row[TradePostCommentTable.createdAt].toString()),
                updatedAt = LocalDateTime.parse(row[TradePostCommentTable.updatedAt].toString()),
            )
        }
    }
}
