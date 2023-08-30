package ukidelly.api.v1.post.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.count
import ukidelly.database.models.comment.CommentTable
import ukidelly.database.models.post.PostTable
import ukidelly.database.models.user.UserTable
import ukidelly.systems.models.Currency
import ukidelly.systems.models.PostCategory
import java.time.LocalDateTime
import java.util.*

@Serializable
data class PostDetail(
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
data class PostPreview(
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

        fun fromResultRow(result: ResultRow): PostPreview {
            return PostPreview(
                postId = result[PostTable.id].value,
                title = result[PostTable.title],
                content = result[PostTable.content],
                userId = result[PostTable.userId].value,
                creator = result[UserTable.userName],
                creatorIsland = result[UserTable.islandName],
                category = result[PostTable.category],
                currency = result[PostTable.currency],
                price = result[PostTable.price],
                closed = result[PostTable.closed],
                commentCount = result[CommentTable.id.count()],
                createdAt = LocalDateTime.parse(result[PostTable.createdAt].toString()),
                updatedAt = LocalDateTime.parse(result[PostTable.updatedAt].toString())
            )

        }
    }
}

