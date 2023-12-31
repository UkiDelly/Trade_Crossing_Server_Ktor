package ukidelly.repositories

import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.entity.FeedCommentsEntity
import ukidelly.database.tables.FeedComments
import ukidelly.dto.requests.Pagination
import ukidelly.models.FeedComment


@Single
class FeedCommentRepository {

  private val logger = LoggerFactory.getLogger(this::class.java)
  suspend fun findAll(feedId: Int, pagination: Pagination): List<FeedComment> {

    val offset = ((pagination.page - 1) * pagination.size).toLong()

    return dbQuery {
      FeedCommentsEntity.find { FeedComments.feedId eq feedId }
        .with(FeedCommentsEntity::parent)
        .limit(pagination.size, offset)
        .orderBy(FeedComments.createdAt to SortOrder.DESC)
        .map { it.toFeedComment() }
    }
  }

}