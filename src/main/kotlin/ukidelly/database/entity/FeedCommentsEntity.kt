package ukidelly.database.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.database.models.user.UserEntity
import ukidelly.database.tables.FeedComments
import ukidelly.models.CompactUser
import ukidelly.models.FeedComment

class FeedCommentsEntity(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<FeedCommentsEntity>(FeedComments)

  var feed by FeedEntity referencedOn FeedComments.feedId
  var content by FeedComments.content
  var parent by FeedComments.parentId
  var user by UserEntity referencedOn FeedComments.userId
  var createdAt by FeedComments.createdAt
  var updatedAt by FeedComments.updatedAt


  fun toFeedComment() = FeedComment(
    id = id.value,
    content = content,
    creator = CompactUser(user),
    parentComment = parent?.value,
    createdAt = createdAt,
    updatedAt = updatedAt
  )


  override fun toString(): String {
    return "FeedCommentsEntity(feed=$feed, content=$content, parent=$parent, user=$user, createdAt=$createdAt, updatedAt=$updatedAt)"
  }
}