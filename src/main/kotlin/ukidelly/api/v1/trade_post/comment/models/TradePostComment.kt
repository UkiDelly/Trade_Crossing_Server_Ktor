package ukidelly.api.v1.trade_post.comment.models//package ukidelly.post.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import ukidelly.database.models.comment.TradePostCommentEntity
import ukidelly.database.models.comment.TradePostComments
import ukidelly.database.models.user.Users
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
                commentId = row[TradePostComments.id].value,
                postId = row[TradePostComments.postId].value,
                content = row[TradePostComments.commentContent],
                parentCommentId = row[TradePostComments.parentCommentId]?.value,
                creator = row[Users.userName],
                creatorIsland = row[Users.islandName],
                createdAt = LocalDateTime.parse(row[TradePostComments.createdAt].toString()),
                updatedAt = LocalDateTime.parse(row[TradePostComments.updatedAt].toString()),
            )
        }
    }
}
