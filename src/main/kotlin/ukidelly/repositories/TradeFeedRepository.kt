package ukidelly.repositories

import io.ktor.server.plugins.*
import org.jetbrains.exposed.dao.with
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.like.TradeFeedLikeEntity
import ukidelly.database.models.like.TradeFeedLikes
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
import ukidelly.dto.requests.CreateTradeFeedRequestDto
import ukidelly.dto.requests.UpdateTradeFeedRequestDto
import ukidelly.models.TradeFeedDetail
import ukidelly.models.TradeFeedPreview
import java.time.LocalDateTime
import java.util.*

@Single
class TradeFeedRepository {

  private val logger = LoggerFactory.getLogger("PostRepository")


  suspend fun findLatestPosts(size: Int, page: Int): Pair<List<TradeFeedPreview>, Int> {
    return dbQuery {
      val totalPage = (TradeFeedEntity.count().toInt() / size).let { if (it == 0) 1 else it }
      val offset = (size * (page - 1)).toLong()
      val feedList =
        TradeFeedEntity.all()
          .with(TradeFeedEntity::user, TradeFeedEntity::comments, TradeFeedEntity::likes)
          .limit(size, offset).map { TradeFeedPreview(it) }

      (feedList to totalPage)
    }
  }


  suspend fun findPost(postId: Int): TradeFeedDetail {
    return dbQuery {
      val feedEntity = TradeFeedEntity.findById(postId) ?: throw NotFoundException()
      TradeFeedDetail(feedEntity)
    }
  }


  suspend fun addNewPost(post: CreateTradeFeedRequestDto, creatorId: UUID): TradeFeedDetail {
    val userEntity = dbQuery { UserEntity.find { Users.uuid eq (creatorId) }.first() }
    val newFeed = dbQuery {
      TradeFeedEntity.new {
        title = post.title
        content = post.content
        user = userEntity
        category = post.category
        currency = post.currency
        price = post.price
        closed = post.closed
      }
    }
    return TradeFeedDetail(newFeed)
  }

  suspend fun updatePost(postId: Int, post: UpdateTradeFeedRequestDto): TradeFeedDetail = dbQuery {
    val tradeEntity = TradeFeedEntity.findById(postId)?.apply {
      title = post.title ?: this.title
      content = post.content ?: this.content
      category = post.category ?: this.category
      currency = post.currency ?: this.currency
      price = post.price
      updatedAt = LocalDateTime.now()
    } ?: throw NotFoundException("게시글을 찾을 수 없습니다.")
    TradeFeedDetail(tradeEntity)
  }


  suspend fun deletePost(postId: Int) {
    dbQuery {
      TradeFeedEntity.findById(postId)?.delete() ?: throw NotFoundException("게시글이 존재하지 않습니다.")
    }
  }

  suspend fun likeFeed(feedId: Int, userId: UUID): TradeFeedDetail {
    return dbQuery {
      val feed = TradeFeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")
      val user =
        UserEntity.find { Users.uuid eq userId }.firstOrNull() ?: throw NotFoundException("존재하지 않는 유저입니다.")
      val likeEntity = TradeFeedLikeEntity.find { TradeFeedLikes.postId eq feedId }.firstOrNull()

      if (likeEntity != null) {
        likeEntity.delete()
        TradeFeedDetail(feed)
      } else {
        TradeFeedLikeEntity.new {
          this.post = feed.id
          this.user = user.id
        }
        TradeFeedDetail(feed)
      }
    }
  }
}
