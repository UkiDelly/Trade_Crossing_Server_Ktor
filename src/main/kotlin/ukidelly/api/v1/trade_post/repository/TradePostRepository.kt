package ukidelly.api.v1.trade_post.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.api.v1.trade_post.models.TradePostCreateRequest
import ukidelly.api.v1.trade_post.models.TradePostDetail
import ukidelly.api.v1.trade_post.models.TradePostPreview
import ukidelly.api.v1.trade_post.models.TradePostUpdateRequest
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.comment.TradePostCommentTable
import ukidelly.database.models.post.TradePostEntity
import ukidelly.database.models.post.TradePostTable
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.UserTable
import java.time.LocalDateTime
import java.util.*

@Single
class TradePostRepository {

    private val logger = LoggerFactory.getLogger("PostRepository")


    suspend fun findLatestPosts(size: Int, page: Int): Pair<List<TradePostPreview>, Int> {

        val totalCount = dbQuery { TradePostTable.selectAll().count() }
        val totalPage = (totalCount / size).toInt().let {
            if (it == 0) 1 else it
        }
        val offset = ((page - 1) * size).toLong()
        val posts = dbQuery {
            TradePostTable
                .join(
                    otherTable = TradePostCommentTable,
                    onColumn = TradePostTable.id,
                    otherColumn = TradePostCommentTable.postId,
                    joinType = JoinType.LEFT
                )
                .join(
                    otherTable = UserTable,
                    onColumn = TradePostTable.userUUID,
                    otherColumn = UserTable.id,
                    joinType = JoinType.LEFT
                )
                .slice(
                    TradePostTable.columns + UserTable.userName + UserTable.islandName + TradePostCommentTable.id.count()
                )
                .selectAll()
                .limit(size, offset)
                .groupBy(TradePostTable.id, TradePostTable.createdAt, UserTable.userName, UserTable.islandName)
                .orderBy(TradePostTable.createdAt to SortOrder.DESC)
                .toList().map {
                    TradePostPreview.fromResultRow(it)
                }
        }

        return posts to totalPage
    }


    suspend fun findPost(postId: Int): TradePostDetail? {

        val postEntity = dbQuery { TradePostEntity.findById(postId) } ?: return null
        val userEntity = dbQuery { UserEntity.find { UserTable.uuid.eq(postEntity.userUUID) }.first() }
        return TradePostDetail(
            postId = postEntity.id.value,
            title = postEntity.title,
            content = postEntity.content,
            category = postEntity.category,
            creator = userEntity.userName,
            creatorIsland = userEntity.islandName,
            createdAt = postEntity.createdAt,
            updatedAt = postEntity.updatedAt,
            price = postEntity.price,
            currency = postEntity.currency,
            closed = postEntity.closed,
        )
    }


    suspend fun addNewPost(post: TradePostCreateRequest, creatorId: UUID): EntityID<Int> {
        return dbQuery {
            TradePostTable.insertAndGetId {
                it[title] = post.title
                it[content] = post.content
                it[userUUID] = creatorId
                it[category] = post.category
                it[currency] = post.currency
                it[price] = post.price
                it[closed] = post.closed
                it[createdAt] = LocalDateTime.now()
                it[updatedAt] = LocalDateTime.now()
            }
        }
    }

    suspend fun updatePost(postId: Int, post: TradePostUpdateRequest): TradePostEntity? {
        val postEntity = dbQuery { TradePostEntity.findById(postId) }?.apply {
            title = post.title ?: this.title
            content = post.content ?: this.content
            category = post.category ?: this.category
            currency = post.currency ?: this.currency
            price = post.price ?: this.price
            closed = post.closed ?: this.closed
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