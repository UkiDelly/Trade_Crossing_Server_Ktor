package ukidelly.repositories

import io.ktor.server.plugins.*
import org.jetbrains.exposed.dao.with
import org.koin.core.annotation.Single
import org.slf4j.Logger
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
    val logger: Logger = LoggerFactory.getLogger("FeedRepository")


    suspend fun findLatestFeed(size: Int, page: Int): Pair<Int, List<FeedPreview>> =
        dbQuery {
            val totalPage = (FeedEntity.count().toInt() / size) + 1
            val offset = (size * (page - 1)).toLong()

            (totalPage to FeedEntity.all().with(FeedEntity::user, FeedEntity::images, FeedEntity::likes)
                .limit(size, offset)
                .map { FeedPreview(it) }
                .reversed())
        }


    suspend fun findFeedById(feedId: Int): Feed = dbQuery {
        val feedEntity = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")

        Feed(feedEntity)
    }


    suspend fun addNewFeed(uuid: UUID, content: String, images: List<String>): Feed {
        return dbQuery {
            val user = UserEntity.find { Users.uuid eq uuid }.firstOrNull() ?: throw NotFoundException("유저가 존재하지 않습니다.")
            var feedEntity = FeedEntity.new { this.user = user; this.content = content }
            val feedId = feedEntity.id.value
            val imageEntities = images.map { ImageEntity.new { this.url = it } }
            imageEntities.map { FeedImageEntity.new { this.post = feedEntity; this.image = it } }
            feedEntity = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")

            Feed(feedEntity)
        }
    }

    suspend fun updateFeed(feedId: Int, content: String?, images: List<String>): Feed {
        return dbQuery {
            var feedEntity = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")

            if (content != null) feedEntity.content = content
            if (images.isEmpty()) return@dbQuery Feed(feedEntity)

            val imageEntities = images.map { ImageEntity.new { this.url = it } }
            imageEntities.map { FeedImageEntity.new { this.post = feedEntity; this.image = it } }
            feedEntity = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")

            Feed(feedEntity)
        }
    }

    suspend fun deleteImages(imageIds: List<Int>): List<String> {
        return dbQuery {
            val imageUrls = imageIds.map { ImageEntity.findById(it)!!.url }
            imageIds.forEach { ImageEntity.findById(it)!!.delete() }

            imageUrls
        }
    }
}