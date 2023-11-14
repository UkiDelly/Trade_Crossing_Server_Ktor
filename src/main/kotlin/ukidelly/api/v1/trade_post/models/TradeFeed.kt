package ukidelly.api.v1.trade_post.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.systems.models.CreateAtUpdateAtBase
import ukidelly.systems.models.Currency
import ukidelly.systems.models.PostCategory

@Serializable
data class TradeFeedDetail(
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
data class TradeFeedPreview(
    val postId: Int,
    val title: String,
    val content: String,
    val creator: String,
    val creatorIsland: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val commentCount: Int,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime
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
        commentCount = 0,
        createdAt = LocalDateTime.parse(entity.createdAt.toString()),
        updatedAt = LocalDateTime.parse(entity.updatedAt.toString()),
    )

}