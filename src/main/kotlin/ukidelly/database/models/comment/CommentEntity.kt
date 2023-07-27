package ukidelly.database.models.comment

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import ukidelly.database.models.user.UserTable

class CommentEntity(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<CommentEntity>(CommentTable)

    var postId by CommentTable.postId
    var parentCommentId by CommentTable.parentCommentId
    var content by CommentTable.content
    var creator by CommentTable.creator
    var creatorIsland by CommentTable.creatorIsland
    var createdAt by CommentTable.createdAt
    var updatedAt by CommentTable.updatedAt
}


object CommentTable : IntIdTable("comment", "comment_id") {
    val postId = integer("post_id")
    val parentCommentId = integer("parent_comment_id").nullable()
    val content = text("content")
    val creator = reference("creator", UserTable.userName)
    val creatorIsland = reference("creator_island", UserTable.islandName)
    val createdAt = varchar("created_at", 255)
    val updatedAt = varchar("updated_at", 255)


}