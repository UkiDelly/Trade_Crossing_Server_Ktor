package ukidelly.repositories

import io.ktor.server.plugins.*
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.entity.FeedEntity
import ukidelly.database.entity.FeedImageEntity
import ukidelly.database.entity.ImageEntity
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
import ukidelly.models.Feed
import ukidelly.models.FeedPreview
import java.util.*


@Single
class FeedRepository {
    val logger = LoggerFactory.getLogger("FeedRepository")


    suspend fun findLatestFeed(size: Int, page: Int): Pair<Int, List<FeedPreview>> =
        dbQuery {
            val totalPage = (FeedEntity.count().toInt() / size).let {
                if (it == 0) 1 else it
            }
            val offset = (size * (page - 1)).toLong()
            totalPage to FeedEntity.all().limit(size, offset).map { FeedPreview(it) }
        }


    suspend fun findFeedById(feedId: Int): Feed = dbQuery {
        val feedEntity = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")
        Feed(feedEntity)
    }


    suspend fun addNewFeed(uuid: UUID, content: String, images: List<String>): Feed {
        return dbQuery {
            val user = UserEntity.find { Users.uuid eq uuid }.firstOrNull() ?: throw NotFoundException("유저가 존재하지 않습니다.")
            var feedEntity = FeedEntity.new {
                this.user = user
                this.content = content
            }

            val feedId = feedEntity.id.value

            val imageEntities = images.map {
                ImageEntity.new {
                    this.url = it
                }
            }

            imageEntities.map {
                FeedImageEntity.new {
                    this.post = feedEntity
                    this.image = it
                }
            }

            feedEntity = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")
            Feed(feedEntity)
        }
    }
}