package ukidelly.database.models.post

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.entity.FeedCommentsEntity
import ukidelly.database.models.like.TradeFeedLikeEntity
import ukidelly.database.models.like.TradeFeedLikes
import ukidelly.database.models.user.UserEntity
import ukidelly.database.tables.FeedComments

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
    val comments by FeedCommentsEntity referrersOn FeedComments.feedId
    val likes by TradeFeedLikeEntity referrersOn TradeFeedLikes.postId
}
