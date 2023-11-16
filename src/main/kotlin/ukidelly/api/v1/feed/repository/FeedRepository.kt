package ukidelly.api.v1.feed.repository

import io.ktor.server.plugins.*
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.api.v1.feed.comment.models.FeedComment
import ukidelly.api.v1.feed.models.Feed
import ukidelly.api.v1.feed.models.FeedPreviewModel
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.entity.FeedEntity
import java.util.*


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

    suspend fun findFeedById(feedId: Int): Pair<Feed, List<FeedComment>> {
        return dbQuery {
            val feedEntity = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")
            val feed = Feed(feedEntity)
            val comments = feedEntity.comments.map { FeedComment(it) }
            feed to comments
        }
    }


    suspend fun addNewFeed(uuid: UUID, content: String, images: List<String>): List<String> {

        return dbQuery {
            emptyList<String>()
        }
    }
}