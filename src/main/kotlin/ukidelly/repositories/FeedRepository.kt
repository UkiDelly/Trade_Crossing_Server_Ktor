package ukidelly.repositories

import io.ktor.server.plugins.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.entity.FeedEntity
import ukidelly.database.entity.FeedImageEntity
import ukidelly.database.entity.ImageEntity
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
import ukidelly.database.tables.FeedImages
import ukidelly.database.tables.Feeds
import ukidelly.database.tables.Images
import ukidelly.models.Feed
import ukidelly.models.FeedPreview
import ukidelly.modules.SupabaseServerClient
import java.util.*


@Single
class FeedRepository {
  val logger: Logger = LoggerFactory.getLogger("FeedRepository")
  private val supabaseClient by KoinJavaComponent.inject<SupabaseServerClient>(clazz = SupabaseServerClient::class.java)


  suspend fun findLatestFeed(size: Int, page: Int): Pair<Int, List<FeedPreview>> =
    dbQuery {
      val totalPage = (FeedEntity.count().toInt() / size) + 1
      val offset = (size * (page - 1)).toLong()

      val feedPreviews =
        FeedEntity.all().limit(size, offset).with(
          FeedEntity::user,
          FeedEntity::images,
          FeedEntity::likes,
          FeedEntity::comments,
          FeedEntity::likes
        ).reversed().map { FeedPreview(it) }

      (totalPage to feedPreviews)
    }


  suspend fun findFeedById(feedId: Int): Feed = dbQuery {
    val feedEntity = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")

    Feed(feedEntity)
  }


  suspend fun addNewFeed(uuid: UUID, content: String, images: List<String>): Feed {
    return dbQuery {
      val user = UserEntity.find { Users.uuid eq uuid }.firstOrNull()
        ?: throw NotFoundException("유저가 존재하지 않습니다.")

      val imageEntities = Images.batchInsert(images) { url ->
        this[Images.url] = url
      }.map { ImageEntity.wrapRow(it) }

      val feedEntity = Feeds.insertAndGetId {
        it[creator] = user.id
        it[Feeds.content] = content
      }.let {
        FeedEntity.findById(it)!!
      }
      FeedImages.batchInsert(imageEntities) { imageEntity ->
        this[FeedImages.feedId] = feedEntity.id
        this[FeedImages.imageId] = imageEntity.id
      }

      Feed(feedEntity)
    }
  }

  suspend fun updateFeed(feedId: Int, content: String?, images: List<String>, deleteImages: List<Int>): Feed {
    return dbQuery {
      var feedEntity = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")

      if (content != null) feedEntity.content = content
      if (images.isEmpty()) return@dbQuery Feed(feedEntity)

      val imageEntities = images.map { ImageEntity.new { this.url = it } }
      imageEntities.map { FeedImageEntity.new { this.post = feedEntity; this.image = it } }
      feedEntity.refresh(flush = true)

      Feed(feedEntity)
    }
  }

  suspend fun deleteFeed(feedId: Int) {
    dbQuery {
      val feed = FeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")
      val imageIds = feed.images.map { it.id.value }

      if (imageIds.isNotEmpty()) {
        val imageUrls = deleteAndGetImagesUrl(imageIds)
        withContext(Dispatchers.IO) { supabaseClient.deleteImage(imageUrls) }
      }

      feed.delete()
    }
  }


  private fun deleteAndGetImagesUrl(imageIds: List<Int>): List<String> {
    val images = ImageEntity.find { Images.id inList imageIds }.toList()
    val urls = images.map { it.url }
    Images.deleteWhere { Images.id inList imageIds }

    return urls
  }

}