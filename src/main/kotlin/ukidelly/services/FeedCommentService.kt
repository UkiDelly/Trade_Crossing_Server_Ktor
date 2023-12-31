package ukidelly.services

import org.koin.core.annotation.Single
import ukidelly.dto.requests.CreateFeedCommentRequest
import ukidelly.dto.responses.FeedCommentDto
import ukidelly.repositories.FeedCommentRepository
import java.util.*


@Single
class FeedCommentService(private val feedCommentRepository: FeedCommentRepository) {

  suspend fun getFeedComments(feedId: Int): List<FeedCommentDto> {
    val comments = feedCommentRepository.findAll(feedId)
    val parentComments = comments.filter { it.parentComment == null }

    val dtos = parentComments.map {
      val childComments = comments.filter { child -> child.parentComment == it.id }
      FeedCommentDto(it, childComments)
    }
    return dtos
  }

  suspend fun addComment(feedId: Int, userUUID: UUID, body: CreateFeedCommentRequest) =
    feedCommentRepository.newComment(feedId, userUUID, body)

  suspend fun updateComment(commentId: Int, userUUID: UUID, body: CreateFeedCommentRequest) =
    feedCommentRepository.updateComment(commentId, userUUID, body)

  suspend fun deleteComment(commentId: Int, userUUID: UUID) = feedCommentRepository.deleteComment(commentId, userUUID)
}