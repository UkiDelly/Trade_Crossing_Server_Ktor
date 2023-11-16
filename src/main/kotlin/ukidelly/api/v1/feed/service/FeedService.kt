package ukidelly.api.v1.feed.service

import org.koin.core.annotation.Single
import ukidelly.api.v1.feed.comment.models.FeedCommentDto
import ukidelly.api.v1.feed.models.FeedDto
import ukidelly.api.v1.feed.models.FeedPreviewModel
import ukidelly.api.v1.feed.repository.FeedRepository


@Single
class FeedService(private val feedRepository: FeedRepository) {


    suspend fun getLatestPosts(
        page: Int,
        size: Int
    ): List<FeedPreviewModel> {
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