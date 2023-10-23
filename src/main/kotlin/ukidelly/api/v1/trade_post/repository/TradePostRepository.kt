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
import ukidelly.database.models.comment.TradePostComments
import ukidelly.database.models.post.TradePostEntity
import ukidelly.database.models.post.TradePosts
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
import java.time.LocalDateTime
import java.util.*

@Single
class TradePostRepository {

    private val logger = LoggerFactory.getLogger("PostRepository")


    suspend fun findLatestPosts(size: Int, page: Int): Pair<List<TradePostPreview>, Int> {

        val totalCount = dbQuery { TradePosts.selectAll().count() }
        val totalPage = (totalCount / size).toInt().let {
            if (it == 0) 1 else it
        }
        val offset = ((page - 1) * size).toLong()
        val posts = dbQuery {

            val result = TradePosts.leftJoin(Users).leftJoin(TradePostComments)
                .slice(TradePosts.columns + Users.userName + Users.islandName + TradePostComments.id.count())
                .selectAll().limit(size, offset).orderBy(TradePosts.createdAt to SortOrder.DESC).toList()

            result.map {
                TradePostPreview.fromResultRow(it)
            }
        }

        return posts to totalPage
    }


    suspend fun findPost(postId: Int): TradePostEntity? {


        return dbQuery { TradePostEntity.findById(postId) }
    }


    suspend fun addNewPost(post: CreateTradePostRequest, creatorId: UUID): Int {

        val user = dbQuery { UserEntity.find { Users.uuid eq (creatorId) }.first() }

        return dbQuery {
            TradePosts.insertAndGetId {
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

    suspend fun updatePost(postId: Int, post: CreateTradePostRequest): TradePostEntity? {
        val postEntity = dbQuery { TradePostEntity.findById(postId) }?.apply {
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
        val post = withContext(Dispatchers.IO) { dbQuery { TradePostEntity.findById(postId) } } ?: return false
        logger.debug("Deleting Post: {}", post)
//        진짜로 삭제하는 코드 구현
        return true
    }
}