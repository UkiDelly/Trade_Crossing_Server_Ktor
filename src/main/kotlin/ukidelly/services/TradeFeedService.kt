package ukidelly.services

import org.koin.core.annotation.Single
import ukidelly.dto.requests.CreateTradeFeedRequestDto
import ukidelly.dto.responses.LatestTradeFeedDto
import ukidelly.dto.responses.TradeFeedCommentDto
import ukidelly.dto.responses.TradeFeedDto
import ukidelly.models.TradeFeedDetail
import ukidelly.repositories.TradeFeedRepository
import java.util.*


@Single
class TradeFeedService(
    private val tradeFeedRepository: TradeFeedRepository,
    private val tradeFeedCommentService: TradeFeedCommentService
) {

    suspend fun getLatestPosts(itemsPerPage: Int, page: Int): LatestTradeFeedDto {

        // 게시글 정보
        val result = tradeFeedRepository.findLatestPosts(itemsPerPage, page)
        var feedList = result.first
        val totalPage = result.second
        return LatestTradeFeedDto(feedList, page, totalPage)
    }

    suspend fun getPost(postId: Int): TradeFeedDto {
        val result = tradeFeedRepository.findPost(postId)
        val commentList = result.second

        val tradeCommentDtos = mutableListOf<TradeFeedCommentDto>()
        commentList.forEach { parentComment ->
            val childComments = commentList.filter { it.parentCommentId == parentComment.commentId }
                .map { TradeFeedCommentDto(it) }
            tradeCommentDtos.add(TradeFeedCommentDto(parentComment, childComments))
        }

        return TradeFeedDto(result.first, tradeCommentDtos)
    }

    suspend fun addNewPost(newPost: CreateTradeFeedRequestDto, userId: UUID): TradeFeedDetail {
        return tradeFeedRepository.addNewPost(newPost, userId)

    }

    suspend fun updateFeed(feedId: Int, feed: CreateTradeFeedRequestDto): TradeFeedDto {
        val feed = tradeFeedRepository.updatePost(postId = feedId, feed)
        return getPost(feed.postId)
    }

    suspend fun deletePost(postId: Int) {
        tradeFeedRepository.deletePost(postId)
    }

    suspend fun likeFeed(feedId: Int, userId: UUID) {
        tradeFeedRepository.likeFeed(feedId, userId)
    }
}