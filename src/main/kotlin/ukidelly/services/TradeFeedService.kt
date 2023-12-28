package ukidelly.services

import org.koin.core.annotation.Single
import ukidelly.dto.requests.CreateTradeFeedRequestDto
import ukidelly.dto.requests.UpdateTradeFeedRequestDto
import ukidelly.dto.responses.LatestTradeFeedDto
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

    suspend fun getPost(postId: Int): TradeFeedDetail = tradeFeedRepository.findPost(postId)


    suspend fun addNewPost(newPost: CreateTradeFeedRequestDto, userId: UUID): TradeFeedDetail {
        return tradeFeedRepository.addNewPost(newPost, userId)

    }

    suspend fun updateFeed(feedId: Int, feed: UpdateTradeFeedRequestDto): TradeFeedDetail =
        tradeFeedRepository.updatePost(postId = feedId, feed)

    suspend fun deletePost(postId: Int) {
        tradeFeedRepository.deletePost(postId)
    }

    suspend fun likeFeed(feedId: Int, userId: UUID): TradeFeedDetail {
        return tradeFeedRepository.likeFeed(feedId, userId)
    }
}