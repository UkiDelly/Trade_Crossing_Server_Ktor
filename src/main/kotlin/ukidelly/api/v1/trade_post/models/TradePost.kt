package ukidelly.api.v1.trade_post.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.count
import ukidelly.database.models.comment.TradePostCommentTable
import ukidelly.database.models.post.TradePostTable
import ukidelly.database.models.user.UserTable
import ukidelly.systems.models.Currency
import ukidelly.systems.models.PostCategory
import java.time.LocalDateTime
import java.util.*

@Serializable
data class TradePostDetail(
    val postId: Int,
    val title: String,
    val content: String,
    val creator: String,
    val creatorIsland: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val closed: Boolean,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime,
)


@Serializable
data class TradePostPreview(
    val postId: Int,
    val title: String,
    val content: String,
    @Contextual
    val userId: UUID,
    val creator: String,
    val creatorIsland: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val closed: Boolean,
    val commentCount: Long,
    @Contextual
    val createdAt: LocalDateTime,
    @Contextual
    val updatedAt: LocalDateTime,
) {
    companion object {

        fun fromResultRow(result: ResultRow): TradePostPreview {
            return TradePostPreview(
                postId = result[TradePostTable.id].value,
                title = result[TradePostTable.title],
                content = result[TradePostTable.content],
                userId = result[TradePostTable.userUUID],
                creator = result[UserTable.userName],
                creatorIsland = result[UserTable.islandName],
                category = result[TradePostTable.category],
                currency = result[TradePostTable.currency],
                price = result[TradePostTable.price],
                closed = result[TradePostTable.closed],
                commentCount = result[TradePostCommentTable.id.count()],
                createdAt = LocalDateTime.parse(result[TradePostTable.createdAt].toString()),
                updatedAt = LocalDateTime.parse(result[TradePostTable.updatedAt].toString())
            )

        }
    }
}

