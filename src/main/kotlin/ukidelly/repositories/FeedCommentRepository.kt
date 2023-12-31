package ukidelly.repositories

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.entity.FeedCommentsEntity
import ukidelly.database.models.user.Users
import ukidelly.database.tables.FeedComments
import ukidelly.models.FeedComment


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
}