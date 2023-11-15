package ukidelly.api.v1.trade_post.repository

import io.ktor.server.plugins.*
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.api.v1.trade_post.models.CreateTradeFeedRequest
import ukidelly.api.v1.trade_post.models.TradeFeedDetail
import ukidelly.api.v1.trade_post.models.TradeFeedPreview
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
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


    suspend fun findPost(postId: Int): TradeFeedDetail {
        return dbQuery {
            val post = TradeFeedEntity.findById(postId) ?: throw NotFoundException()
            TradeFeedDetail(post)
        }
    }


    suspend fun addNewPost(post: CreateTradeFeedRequest, creatorId: UUID): Int {
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

    suspend fun updatePost(postId: Int, post: CreateTradeFeedRequest): TradeFeedDetail {
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
}
