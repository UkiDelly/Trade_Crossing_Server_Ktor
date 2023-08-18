package ukidelly.api.v1.post.repository

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.koin.core.annotation.Module
import org.slf4j.LoggerFactory
import ukidelly.api.v1.comment.models.Comment
import ukidelly.api.v1.post.models.PostCreateRequest
import ukidelly.api.v1.post.models.PostDetail
import ukidelly.api.v1.post.models.PostPreview
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.comment.CommentEntity
import ukidelly.database.models.comment.CommentTable
import ukidelly.database.models.post.PostEntity
import ukidelly.database.models.post.PostTable
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.UserTable
import java.util.*

@Module
class PostRepository {

	private val logger = LoggerFactory.getLogger("PostRepository")


	suspend fun findLatestPosts(size: Int, page: Int): Pair<List<PostPreview>, Int> {

		val totalCount = dbQuery { PostTable.selectAll().count() }
		val totalPage = (totalCount / size).toInt()
		val offset = ((page - 1) * size).toLong()
		val posts = dbQuery {
			PostTable
				.join(
					otherTable = CommentTable,
					onColumn = PostTable.id,
					otherColumn = CommentTable.postId,
					joinType = JoinType.LEFT
				)
				.join(
					otherTable = UserTable,
					onColumn = PostTable.userId,
					otherColumn = UserTable.id,
					joinType = JoinType.LEFT
				)
				.slice(
					PostTable.columns + UserTable.userName + UserTable.islandName + CommentTable.id.count()
				)
				.selectAll()
				.limit(size, offset)
				.groupBy(PostTable.id, PostTable.createdAt)
				.orderBy(PostTable.createdAt to SortOrder.DESC)
				.toList().map {
					PostPreview.fromResultRow(it)
				}
		}

		return posts to totalPage
	}


	suspend fun getPost(postId: Int): Pair<PostDetail, List<Comment>>? {

		val postEntity = dbQuery { PostEntity.findById(postId) } ?: return null
		val userEntity = dbQuery { UserEntity.findById(postEntity.userId) } ?: return null
		val comments = dbQuery {
			CommentEntity.find {
				CommentTable.postId eq postEntity.id
			}.toList().map {
				Comment.fromEntity(it, userEntity.userName, userEntity.islandName)
			}
		}

		return PostDetail(
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
		) to comments
	}


	suspend fun addNewPost(post: PostCreateRequest, userId: UUID): EntityID<Int> {
		return dbQuery {
			PostTable.insertAndGetId {
				it[title] = post.title
				it[content] = post.content
				it[UserTable.id] = userId
				it[category] = post.category
				it[currency] = post.currency
				it[price] = post.price
				it[closed] = post.closed
			}
		}

	}


	suspend fun deletePost(postId: Int, userId: UUID): Boolean {
		val result = dbQuery {
			PostTable.deleteWhere {
				(PostTable.id eq postId) and (PostTable.userId eq userId)
			}
		}
		return true

	}
}