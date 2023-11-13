package ukidelly.api.v1.trade_post.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.api.v1.trade_post.models.CreateTradePostRequest
import ukidelly.api.v1.trade_post.models.TradePostPreview
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.comment.TradeFeedComments
import ukidelly.database.models.post.TradeFeedEntity
import ukidelly.database.models.post.TradeFeeds
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
import java.time.LocalDateTime
import java.util.*

@Single
class TradeFeedRepository {

    private val logger = LoggerFactory.getLogger("PostRepository")


    suspend fun findLatestPosts(size: Int, page: Int): Pair<List<TradePostPreview>, Int> {

        val totalCount = dbQuery { TradeFeeds.selectAll().count() }
        val totalPage = (totalCount / size).toInt().let {
            if (it == 0) 1 else it
        }
        val offset = ((page - 1) * size).toLong()
        val posts = dbQuery {

            val result = TradeFeeds.leftJoin(Users).leftJoin(TradeFeedComments)
                .slice(TradeFeeds.columns + Users.userName + Users.islandName + TradeFeedComments.id.count())
                .selectAll().limit(size, offset).orderBy(TradeFeeds.createdAt to SortOrder.DESC).toList()

            result.map {
                TradePostPreview(it)
            }
        }

        return posts to totalPage
    }


    suspend fun findPost(postId: Int): TradeFeedEntity? {


        return dbQuery { TradeFeedEntity.findById(postId) }
    }


    suspend fun addNewPost(post: CreateTradePostRequest, creatorId: UUID): Int {

        val user = dbQuery { UserEntity.find { Users.uuid eq (creatorId) }.first() }

        return dbQuery {
            TradeFeeds.insertAndGetId {
                it[title] = post.title
                it[content] = post.content
                it[user_id] = user.id
                it[category] = post.category
                it[currency] = post.currency
                it[price] = post.price
                it[closed] = post.closed
                it[createdAt] = LocalDateTime.now()
                it[updatedAt] = LocalDateTime.now()
            }
        }.value
    }

    suspend fun updatePost(postId: Int, post: CreateTradePostRequest): TradeFeedEntity? {
        val postEntity = dbQuery { TradeFeedEntity.findById(postId) }?.apply {
            title = post.title
            content = post.content
            category = post.category
            currency = post.currency
            price = post.price ?: this.price
            closed = post.closed
            updatedAt = LocalDateTime.now()
        }
        return postEntity
    }


    suspend fun deletePost(postId: Int): Boolean {
        val post = withContext(Dispatchers.IO) { dbQuery { TradeFeedEntity.findById(postId) } } ?: return false
        logger.debug("Deleting Post: {}", post)
        //        진짜로 삭제하는 코드 구현
        return true
    }
}