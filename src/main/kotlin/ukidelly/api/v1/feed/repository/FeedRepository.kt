package ukidelly.api.v1.feed.repository

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.annotation.Module
import org.slf4j.LoggerFactory
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.user.UserTable
import ukidelly.database.tables.FeedTable
import ukidelly.database.tables.ImageTable


@Module
class FeedRepository {
    val logger = LoggerFactory.getLogger("FeedRepository")


    suspend fun findLatestFeed(size: Int, page: Int) {
        val feedList = dbQuery {
            FeedTable.leftJoin(ImageTable).slice(FeedTable.columns + ImageTable.url).selectAll()
                .limit(size, ((page - 1) * size).toLong())
                .groupBy(FeedTable.id)
                .orderBy(FeedTable.createdAt to SortOrder.DESC).toList()
        }


    }


    suspend fun addNewFeed(userId: Int, content: String, images: List<String>) {

        dbQuery { _ ->
            FeedTable.insert {
                it[UserTable.id] = userId
                it[FeedTable.content] = content
            }
        }
    }
}