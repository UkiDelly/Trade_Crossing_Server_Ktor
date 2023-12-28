package ukidelly.services

import org.koin.core.annotation.Single
import ukidelly.dto.responses.FeedCommentDto
import ukidelly.dto.responses.FeedDto
import ukidelly.models.FeedPreview
import ukidelly.repositories.FeedRepository


@Single
class FeedService(private val feedRepository: FeedRepository) {


    suspend fun getLatestPosts(page: Int, size: Int): List<FeedPreview> {
        return feedRepository.findLatestFeed(size, page)

    }

    suspend fun getFeedById(feedId: Int): FeedDto {

        val result = feedRepository.findFeedById(feedId)
        val commentDtos = mutableListOf<FeedCommentDto>()
        val comments = result.second

        comments.forEach { parentComment ->
            val childComments = comments.filter { it.parentComment == parentComment.id }
            val commentDto = FeedCommentDto(parentComment, childComments)
            commentDtos.add(commentDto)
        }
        return FeedDto(result.first, commentDtos)
    }
}