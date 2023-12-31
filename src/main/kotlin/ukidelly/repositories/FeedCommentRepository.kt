package ukidelly.repositories

import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.entity.FeedCommentsEntity
import ukidelly.database.models.user.Users
import ukidelly.database.tables.FeedComments
import ukidelly.dto.requests.CreateFeedCommentRequest
import ukidelly.models.FeedComment
import ukidelly.systems.errors.ForbiddenException
import java.util.*


@Single
class FeedCommentRepository {

  private val logger = LoggerFactory.getLogger(this::class.java)
  suspend fun findAll(feedId: Int): List<FeedComment> {

    return dbQuery {
      FeedComments.leftJoin(Users)
        .slice(FeedComments.columns + Users.userName + Users.islandName + Users.defaultProfile)
        .select { FeedComments.feedId eq feedId }
        .orderBy(FeedComments.createdAt to SortOrder.DESC)
        .map {
          logger.info(it.toString())
          FeedCommentsEntity.wrapRow(it).toFeedComment()
        }
    }
  }

  suspend fun newComment(feedId: Int, userUUID: UUID, body: CreateFeedCommentRequest) {
    return dbQuery {
      FeedComments.insert {
        it[content] = body.content
        it[userId] = Users.select { Users.uuid eq userUUID }.first()[Users.id]
        it[FeedComments.feedId] = feedId
        it[parentId] = body.parentComment
      }
    }

  }

  suspend fun updateComment(commentId: Int, userUUID: UUID, body: CreateFeedCommentRequest) {
    return dbQuery {
      val feedEntity = FeedCommentsEntity.findById(commentId) ?: throw NotFoundException("댓글을 찾을 수 없습니다.")
      if (feedEntity.user.uuid != userUUID) throw ForbiddenException()
      feedEntity.content = body.content
    }
  }
}