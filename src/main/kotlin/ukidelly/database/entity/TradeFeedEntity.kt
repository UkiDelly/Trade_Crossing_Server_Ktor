package ukidelly.database.models.post

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.models.comment.TradeFeedCommentEntity
import ukidelly.database.models.comment.TradeFeedComments
import ukidelly.database.models.like.TradeFeedLikeEntity
import ukidelly.database.models.like.TradeFeedLikes
import ukidelly.database.models.user.UserEntity

class TradeFeedEntity(
    id: EntityID<Int>
) : IntEntity(id) {
    companion object : IntEntityClass<TradeFeedEntity>(TradeFeeds)

    var title by TradeFeeds.title
    var content by TradeFeeds.content
    var user by UserEntity referencedOn TradeFeeds.user_id
    var category by TradeFeeds.category
    var currency by TradeFeeds.currency
    var price by TradeFeeds.price
    var closed by TradeFeeds.closed
    var createdAt by TradeFeeds.createdAt
    var updatedAt by TradeFeeds.updatedAt
    val comments by TradeFeedCommentEntity referrersOn TradeFeedComments.postId
    val likes by TradeFeedLikeEntity referrersOn TradeFeedLikes.postId
}
