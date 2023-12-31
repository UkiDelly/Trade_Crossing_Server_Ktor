package ukidelly.services

import org.koin.core.annotation.Single
import ukidelly.dto.requests.Pagination
import ukidelly.dto.responses.FeedCommentDto
import ukidelly.repositories.FeedCommentRepository


@Single
class FeedCommentService(private val feedCommentRepository: FeedCommentRepository) {

  suspend fun getFeedComments(feedId: Int, pagination: Pagination): List<FeedCommentDto> {
    val comments = feedCommentRepository.findAll(feedId, pagination)
    val parentComments = comments.filter { it.parentComment == null }

    val dtos = parentComments.map {
      val childComments = comments.filter { child -> child.parentComment == it.id }
      FeedCommentDto(it, childComments)
    }
    return dtos
  }
}