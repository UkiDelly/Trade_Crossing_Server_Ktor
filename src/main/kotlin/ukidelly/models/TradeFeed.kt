package ukidelly.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.systems.models.Currency
import ukidelly.systems.models.PostCategory

@Serializable
data class TradeFeedDetail(
    val postId: Int,
    val title: String,
    val content: String,
    val creator: CompactUser,
    val creatorIsland: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val closed: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {

    constructor(entity: TradeFeedEntity) : this(
        postId = entity.id.value,
        title = entity.title,
        content = entity.content,
        creator = CompactUser(entity.user),
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
    val creator: CompactUser,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val commentCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val likes: List<Int>
) {

    constructor(entity: TradeFeedEntity) : this(
        postId = entity.id.value,
        title = entity.title,
        content = entity.content,
        creator = CompactUser(entity.user),
        category = entity.category,
        currency = entity.currency,
        price = entity.price,
        commentCount = entity.comments.count().toInt(),
        createdAt = LocalDateTime.parse(entity.createdAt.toString()),
        updatedAt = LocalDateTime.parse(entity.updatedAt.toString()),
        likes = entity.likes.map { it.id.value }
    )

}