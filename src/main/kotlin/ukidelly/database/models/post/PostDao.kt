package ukidelly.database.models.post

import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.LoggerFactory
import ukidelly.api.v1.post.models.PostDto
import ukidelly.api.v1.post.models.PostPreview
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.comment.CommentDao
import ukidelly.database.models.comment.CommentTable
import ukidelly.database.models.user.UserDao
import ukidelly.database.models.user.UserTable
import ukidelly.utils.Utils
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


object PostDao {

    private val logger = LoggerFactory.getLogger("PostDao")


    private val commentDao = CommentDao

    private val userDao = UserDao


    fun entityToPostPreview(entity: PostEntity, count: Int) = PostPreview(
        postId = entity.id.value,
        title = entity.title,
        content = entity.content,
        creator = entity.creator,
        creatorIsland = entity.creatorIsland,
        createdAt = Utils.convertStringToLocalDateTime(entity.createdAt),
        updatedAt = Utils.convertStringToLocalDateTime(entity.updatedAt),
        category = entity.category,
        currency = entity.currency,
        price = entity.price,
        closed = entity.closed,
        commentCount = count
    )


    suspend fun getPosts(itemsPerPage: Int, page: Int, postId: Int?): List<PostPreview> {

        val posts = dbQuery {
            val totalCount = PostTable.selectAll().count()
            val totalPage = totalCount / itemsPerPage
            val offset = ((page - 1) * itemsPerPage).toLong()

            // 서브 쿼리로, post에 해당하는 댓글의 개수를 구합니다.
            val subQuery = CommentTable
                .slice(CommentTable.postId, CommentTable.id.count())
                .selectAll()
                .groupBy(CommentTable.postId)
                .alias("subQuery")


            val postsWithCommentCount = if (postId == null) {
                PostTable.leftJoin(subQuery).selectAll().limit(itemsPerPage)
                    .sortedByDescending { PostTable.createdAt }

            } else {
                PostTable.leftJoin(subQuery).selectAll().limit(itemsPerPage, offset)
                    .sortedByDescending { PostTable.createdAt }
            }

            logger.info("postsWithCommentCount: $postsWithCommentCount")


        }
        return emptyList()
    }

    suspend fun addPost(post: PostDto, userId: UUID) {

        dbQuery {

            PostTable.insert {


                it[title] = post.title
                it[content] = post.content
                it[category] = post.category
                it[currency] = post.currency
                it[price] = post.price
                it[UserTable.id] = userId
                it[closed] = false
                it[createdAt] = LocalDateTime.now(ZoneId.systemDefault()).toString()
                it[updatedAt] = LocalDateTime.now(ZoneId.systemDefault()).toString()
            }

        }
    }
}