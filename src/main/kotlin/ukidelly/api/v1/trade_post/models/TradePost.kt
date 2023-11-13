package ukidelly.api.v1.trade_post.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.count
import ukidelly.database.models.comment.TradeFeedComments
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.database.models.post.TradeFeeds
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

    constructor(entity: TradeFeedEntity) : this(
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

    constructor(result: ResultRow) : this(
        postId = result[TradeFeeds.id].value,
        title = result[TradeFeeds.title],
        content = result[TradeFeeds.content],
        creator = result[Users.userName],
        creatorIsland = result[Users.islandName],
        category = result[TradeFeeds.category],
        currency = result[TradeFeeds.currency],
        price = result[TradeFeeds.price],
        commentCount = result[TradeFeedComments.id.count()],
        createdAt = LocalDateTime.parse(result[TradeFeeds.createdAt].toString()),
        updatedAt = LocalDateTime.parse(result[TradeFeeds.updatedAt].toString())
    )

}