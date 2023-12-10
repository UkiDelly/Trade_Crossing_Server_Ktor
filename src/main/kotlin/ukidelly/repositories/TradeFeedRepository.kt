package ukidelly.repositories

import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.insert
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.like.TradeFeedLikeEntity
import ukidelly.database.models.like.TradeFeedLikes
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
import ukidelly.dto.requests.CreateTradeFeedRequestDto
import ukidelly.models.TradeFeedComment
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
            val feedList = TradeFeedEntity.all().limit(size, offset).map { TradeFeedPreview(it) }

            (feedList to totalPage)
        }

    }


    suspend fun findPost(postId: Int): Pair<TradeFeedDetail, List<TradeFeedComment>> {
        return dbQuery {
            val feedEntity = TradeFeedEntity.findById(postId) ?: throw NotFoundException()
            val feed = TradeFeedDetail(feedEntity)
            val comments = feedEntity.comments.map { TradeFeedComment(it) }
            feed to comments
        }
    }


    suspend fun addNewPost(post: CreateTradeFeedRequestDto, creatorId: UUID): Int {
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
            }.id
        }
        return newFeed.value
    }

    suspend fun updatePost(postId: Int, post: CreateTradeFeedRequestDto): TradeFeedDetail {
        val postEntity = dbQuery { TradeFeedEntity.findById(postId) }?.apply {
            title = post.title
            content = post.content
            category = post.category
            currency = post.currency
            price = post.price ?: this.price
            closed = post.closed
            updatedAt = LocalDateTime.now()
        } ?: throw NotFoundException()
        return TradeFeedDetail(postEntity)
    }


    suspend fun deletePost(postId: Int) {
        dbQuery {
            TradeFeedEntity.findById(postId)?.delete() ?: throw NotFoundException("게시글이 존재하지 않습니다.")
        }
    }

    suspend fun likeFeed(feedId: Int, userId: UUID): String {
        return dbQuery {
            val feed = TradeFeedEntity.findById(feedId) ?: throw NotFoundException("게시글이 존재하지 않습니다.")
            val user =
                UserEntity.find { Users.uuid eq userId }.firstOrNull() ?: throw NotFoundException("존재하지 않는 유저입니다.")
            val likeEntity =
                TradeFeedLikeEntity.find { TradeFeedLikes.postId eq feed.id }.find { it.user == user }

            if (likeEntity != null) {
                likeEntity.delete()
                "unlike"
            } else {
                TradeFeedLikes.insert {
                    it[postId] = feed.id
                    it[TradeFeedLikes.userId] = user.id
                }
                "like"
            }
        }
    }
}
