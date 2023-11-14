package ukidelly.api.v1.trade_post.comment.models //package ukidelly.post.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import ukidelly.database.models.comment.TradeFeedCommentEntity
import ukidelly.database.models.comment.TradeFeedComments
import ukidelly.database.models.user.Users
import java.time.LocalDateTime


@Serializable
data class TradeFeedComment(
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
    /**
     * ResultRow에서 TradeFeedComment을 생성하는 생성자
     * @param row [ResultRow] ResultRow 객체
     */
    constructor(row: ResultRow) : this(
        commentId = row[TradeFeedComments.id].value,
        postId = row[TradeFeedComments.postId].value,
        content = row[TradeFeedComments.commentContent],
        parentCommentId = row[TradeFeedComments.parentCommentId]?.value,
        creator = row[Users.userName],
        creatorIsland = row[Users.islandName],
        createdAt = LocalDateTime.parse(row[TradeFeedComments.createdAt].toString()),
        updatedAt = LocalDateTime.parse(row[TradeFeedComments.updatedAt].toString()),
    )


    /**
     * Entity에서 TradeFeedComment을 생성하는 생성자
     * @param entity [TradeFeedCommentEntity] Entity 객체
     */
    constructor(entity: TradeFeedCommentEntity) : this(
        postId = entity.postId.value,
        commentId = entity.id.value,
        parentCommentId = entity.parentCommentId?.value,
        content = entity.commentContent,
        creator = entity.user.userName,
        creatorIsland = entity.user.islandName,
        createdAt = LocalDateTime.parse(entity.createdAt.toString()),
        updatedAt = LocalDateTime.parse(entity.updatedAt.toString()),
    )
}
