package ukidelly.api.v1.feed.repository

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.user.Users
import ukidelly.database.tables.Feeds
import ukidelly.database.tables.ImageTable


@Single
class FeedRepository {
    val logger = LoggerFactory.getLogger("FeedRepository")


    suspend fun findLatestFeed(size: Int, page: Int) {
        val feedList = dbQuery {
            Feeds.leftJoin(ImageTable).slice(Feeds.columns + ImageTable.url).selectAll()
                .limit(size, ((page - 1) * size).toLong())
                .groupBy(Feeds.id)
                .orderBy(Feeds.createdAt to SortOrder.DESC).toList()
        }


    }


    suspend fun addNewFeed(userId: Int, content: String, images: List<String>) {

        dbQuery { _ ->
            Feeds.insert {
                it[Users.id] = userId
                it[Feeds.content] = content
            }
        }
    }
}