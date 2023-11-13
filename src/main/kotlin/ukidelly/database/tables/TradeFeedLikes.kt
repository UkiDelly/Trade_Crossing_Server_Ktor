package ukidelly.database.models.like

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ukidelly.database.models.post.TradeFeeds
import ukidelly.database.models.user.Users


object TradeFeedLikes : IntIdTable("TradeFeedLike") {

    val userId = reference("user_id", Users, ReferenceOption.CASCADE)
    val postId = reference("post_id", TradeFeeds, ReferenceOption.CASCADE)
}