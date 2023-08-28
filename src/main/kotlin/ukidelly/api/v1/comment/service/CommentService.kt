package ukidelly.api.v1.comment.service

import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory
import ukidelly.api.v1.comment.models.Comment
import ukidelly.api.v1.comment.models.CommentDto
import ukidelly.database.models.comment.CommentRepository
import ukidelly.database.models.comment.CommentTable


@Module
class CommentService {

	val commentRepository by inject<CommentRepository>(clazz = CommentRepository::class.java)
	val logger = LoggerFactory.getLogger("CommentService")

	suspend fun getAllComment(postId: Int): List<CommentDto> {

		// Repository에서 ResultRow형태의 List를 받음
		val comments = commentRepository.findAllComments(postId)

		// Routing에 전달할 빈 List<CommentDto> 생성
		val commentDtos = mutableListOf<CommentDto>()

		// 부모 댓글 분리
		val parentComments = comments.filter {
			it[CommentTable.parentCommentId] == null
		}.map {
			Comment.fromRow(it)
		}

		// 대댓글 분리
		val childComments = comments.filter {
			it[CommentTable.parentCommentId] != null
		}.map { Comment.fromRow(it) }

		// 부모 댓글 for-loop
		for (comment in parentComments) {

			// 새로운 childComments가 빈배열인 CommentDto 생성
			var commentDto: CommentDto = CommentDto.fromComment(
				comment = comment,
				childComments = emptyList()
			)

			// 부모댓글의 대댓글들을 담기 위한 List를 생성
			var childCommentDtos = mutableListOf<CommentDto>()

			// 분리한 대댓글 리스트에서 for-loop
			for (childComment in childComments) {

				// 대댓글의 parentCommentId가 부모댓글의 commentId와 일치한지 확인
				if (comment.commentId == childComment.parentCommentId) {

					// 일치하면 새로운 CommentDto를 생성하여 childCommentDto에 담기
					childCommentDtos.add(
						CommentDto.fromComment(
							comment = childComment,
							childComments = emptyList()
						)
					)
				}
			}

			// 대댓글의 요소들을 sort해서 다시 담기
			childCommentDtos = childCommentDtos.sortedBy { it.createdAt }.toMutableList()

			// 부모 댓글 Dto의 childComments에 sort된 childComments을 업데이트
			commentDto = commentDto.copy(childComments = childCommentDtos)
			// 전체의 commentDtos에 새로운 CommentDto를 추가
			commentDtos.add(commentDto)
		}
		logger.debug("comments : {}", commentDtos)
		return commentDtos
	}


}