package ukidelly.database.models.comment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.annotation.Module
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.user.UserTable


@Module
class CommentRepository {

	suspend fun findAllComments(postId: Int): List<ResultRow> {
		return withContext(Dispatchers.IO) {
			dbQuery {
				CommentTable.innerJoin(UserTable).slice(
					CommentTable.id,
					CommentTable.postId,
					CommentTable.parentCommentId,
					CommentTable.commentContent,
					UserTable.id,
					UserTable.userName,
					UserTable.islandName,
					CommentTable.createdAt,
					CommentTable.updatedAt
				).selectAll().toList()
			}
		}
	}

	suspend fun addNewComment(postId: EntityID<Int>) {

		CommentEntity.new {
			this.postId = postId
			this.commentContent = ""
		}
	}
}