package ukidelly.api.v1.trade_post.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.count
import ukidelly.database.models.comment.TradePostComments
import ukidelly.database.models.post.TradePostEntity
import ukidelly.database.models.post.TradePosts
import ukidelly.database.models.user.Users
import ukidelly.systems.models.CreateAtUpdateAtBase
import ukidelly.systems.models.Currency
import ukidelly.systems.models.PostCategory

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
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime,
) : CreateAtUpdateAtBase {


    companion object {
        fun fromEntity(entity: TradePostEntity): TradePostDetail {
            return TradePostDetail(
                postId = entity.id.value,
                title = entity.title,
                content = entity.content,
                creator = entity.user.userName,
                creatorIsland = entity.user.islandName,
                category = entity.category,
                currency = entity.currency,
                price = entity.price,
                closed = entity.closed,
                createdAt = LocalDateTime.parse(entity.createdAt.toString()),
                updatedAt = LocalDateTime.parse(entity.updatedAt.toString()),
            )
        }
    }
}


//@Serializable
//data class TradePostPreview(
//    val postId: Int,
//    val title: String,
//    val content: String,
//    @Contextual
//    val userId: UUID,
//    val creator: String,
//    val creatorIsland: String,
//    val category: PostCategory,
//    val currency: Currency,
//    val price: Int?,
//    val closed: Boolean,
//    val commentCount: Long,
//    @Contextual
//    val createdAt: LocalDateTime,
//    @Contextual
//    val updatedAt: LocalDateTime,
//) {
//    companion object {
//
//        fun fromResultRow(result: ResultRow): TradePostPreview {
//            return TradePostPreview(
//                postId = result[TradePosts.id].value,
//                title = result[TradePosts.title],
//                content = result[TradePosts.content],
//                userId = result[TradePosts.userUUID],
//                creator = result[Users.userName],
//                creatorIsland = result[Users.islandName],
//                category = result[TradePosts.category],
//                currency = result[TradePosts.currency],
//                price = result[TradePosts.price],
//                closed = result[TradePosts.closed],
//                commentCount = result[TradePostComments.id.count()],
//                createdAt = LocalDateTime.parse(result[TradePosts.createdAt].toString()),
//                updatedAt = LocalDateTime.parse(result[TradePosts.updatedAt].toString())
//            )
//
//        }
//    }
//}


@Serializable
data class TradePostPreview(
    val postId: Int,
    val title: String,
    val content: String,
    val creator: String,
    val creatorIsland: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val commentCount: Long,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime
) : CreateAtUpdateAtBase {


    companion object {

        fun fromResultRow(result: ResultRow): TradePostPreview {
            return TradePostPreview(
                postId = result[TradePosts.id].value,
                title = result[TradePosts.title],
                content = result[TradePosts.content],
                creator = result[Users.userName],
                creatorIsland = result[Users.islandName],
                category = result[TradePosts.category],
                currency = result[TradePosts.currency],
                price = result[TradePosts.price],
                commentCount = result[TradePostComments.id.count()],
                createdAt = LocalDateTime.parse(result[TradePosts.createdAt].toString()),
                updatedAt = LocalDateTime.parse(result[TradePosts.updatedAt].toString())

            )
        }
    }
}