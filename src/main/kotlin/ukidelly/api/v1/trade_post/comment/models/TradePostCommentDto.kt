package ukidelly.api.v1.trade_post.comment.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TradePostCommentDto(
    val postId: Int,
    val commentId: Int,
    val content: String,
    val creator: String,
    val creatorIsland: String,
    val childComments: List<TradePostCommentDto>,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromComment(
            tradePostComment: TradePostComment,
            childComments: List<TradePostCommentDto>
        ): TradePostCommentDto {
            return TradePostCommentDto(
                postId = tradePostComment.postId,
                commentId = tradePostComment.commentId,
                childComments = childComments,
                content = tradePostComment.content,
                creator = tradePostComment.creator,
                creatorIsland = tradePostComment.creatorIsland,
                createdAt = tradePostComment.createdAt,
                updatedAt = tradePostComment.updatedAt
            )
        }
    }
}
