package ukidelly.database.models.like

import org.jetbrains.exposed.dao.id.IntIdTable
import ukidelly.database.models.post.PostTable
import ukidelly.database.models.user.UserTable

object LikeTable : IntIdTable("like", "id") {
    val userId = reference("user_id", UserTable.id)
    val postId = reference("post_id", PostTable.id)
}