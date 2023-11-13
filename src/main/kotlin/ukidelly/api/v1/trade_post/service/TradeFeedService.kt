package ukidelly.api.v1.trade_post.service

import org.koin.core.annotation.Single
import ukidelly.api.v1.trade_post.comment.service.TradeFeedCommentService
import ukidelly.api.v1.trade_post.models.CreateTradePostRequest
import ukidelly.api.v1.trade_post.models.LatestTradePostDto
import ukidelly.api.v1.trade_post.models.TradePostDetail
import ukidelly.api.v1.trade_post.models.TradePostDetailDto
import ukidelly.api.v1.trade_post.repository.TradeFeedRepository
import java.util.*


@Single
class TradeFeedService(
    private val tradeFeedRepository: TradeFeedRepository,
    private val tradeFeedCommentService: TradeFeedCommentService
) {


    suspend fun getLatestPosts(itemsPerPage: Int, page: Int): LatestTradePostDto {
        val result = tradeFeedRepository.findLatestPosts(itemsPerPage, page)
        return LatestTradePostDto(result.first, page, result.second)
    }

    suspend fun getPost(postId: Int): TradePostDetailDto? {
        val tradePostEntity = tradeFeedRepository.findPost(postId) ?: return null
        return TradePostDetailDto(TradePostDetail(tradePostEntity), emptyList())
    }

    suspend fun addNewPost(newPost: CreateTradePostRequest, userId: UUID): TradePostDetailDto? {
        val newPostId = tradeFeedRepository.addNewPost(newPost, userId)
        return getPost(newPostId)
    }

    suspend fun deletePost(postId: Int) {
        tradeFeedRepository.deletePost(postId)
    }
}