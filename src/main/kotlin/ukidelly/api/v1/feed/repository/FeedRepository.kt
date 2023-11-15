package ukidelly.api.v1.feed.repository

import org.jetbrains.exposed.sql.insert
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.api.v1.feed.models.FeedPreviewModel
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.entity.FeedEntity
import ukidelly.database.models.user.Users
import ukidelly.database.tables.Feeds


@Single
class FeedRepository {
    val logger = LoggerFactory.getLogger("FeedRepository")


    suspend fun findLatestFeed(size: Int, page: Int): List<FeedPreviewModel> {

        return dbQuery {
            var totalPage = (FeedEntity.count().toInt() / size).let {
                if (it == 0) 1 else it
            }
            val offset = (size * (page - 1)).toLong()
            FeedEntity.all().limit(size, offset).map { FeedPreviewModel(it) }
        }
    }


    suspend fun addNewFeed(userId: Int, content: String, images: List<String>) {

        dbQuery {
            Feeds.insert {
                it[Users.id] = userId
                it[Feeds.content] = content
            }
        }
    }
}