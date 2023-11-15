package ukidelly.database.tables

import ukidelly.database.models.user.Users

object FeedComments : BaseTable("FeedComment", "comment_id") {
    val feedId = reference("feed_id", Feeds.id)
    val content = text("content")
    val parentId = reference("parent_id", FeedComments.id).nullable()
    val userId = reference("user_id", Users.id, fkName = "feed_comment_fk")
}