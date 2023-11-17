package ukidelly.dto.responses

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.models.TradeFeedComment
import java.time.LocalDateTime

@Serializable
data class TradeFeedCommentDto(
    val postId: Int,
    val commentId: Int,
    val content: String,
    val creator: String,
    val creatorIsland: String,
    var childComments: List<TradeFeedCommentDto>,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime
) {

    constructor(
        tradeFeedComment: TradeFeedComment,
        childComments: List<TradeFeedCommentDto> = emptyList()
    ) : this(
        postId = tradeFeedComment.postId,
        commentId = tradeFeedComment.commentId,
        childComments = childComments,
        content = tradeFeedComment.content,
        creator = tradeFeedComment.creator,
        creatorIsland = tradeFeedComment.creatorIsland,
        createdAt = tradeFeedComment.createdAt,
        updatedAt = tradeFeedComment.updatedAt
    )

}
