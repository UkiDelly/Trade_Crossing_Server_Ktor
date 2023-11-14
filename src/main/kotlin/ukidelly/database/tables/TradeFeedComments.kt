package ukidelly.database.models.comment

import org.jetbrains.exposed.sql.ReferenceOption
import ukidelly.database.models.post.TradeFeeds
import ukidelly.database.models.user.Users
import ukidelly.database.tables.BaseTable

object TradeFeedComments : BaseTable("TradeFeedComment", "id") {
    val postId = reference("post_id", TradeFeeds.id, onDelete = ReferenceOption.CASCADE)
    val parentCommentId =
        reference("parent_comment_id", TradeFeedComments.id, onDelete = ReferenceOption.CASCADE).nullable()
    val commentContent = text("comment_content")
    val userId = reference("user_id", Users.id)
}