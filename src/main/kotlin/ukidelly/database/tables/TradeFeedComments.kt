package ukidelly.database.models.comment

import ukidelly.database.models.post.TradeFeeds
import ukidelly.database.tables.BaseTable

object TradeFeedComments : BaseTable("TradeFeedComment", "id") {
    val postId = reference("post_id", TradeFeeds.id)
    val parentCommentId = reference("parent_comment_id", TradeFeedComments.id).nullable()
    val commentContent = text("comment_content")
    val userId = uuid("user_id")
}