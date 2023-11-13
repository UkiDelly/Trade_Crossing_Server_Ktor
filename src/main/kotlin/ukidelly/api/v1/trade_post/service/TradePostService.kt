package ukidelly.api.v1.trade_post.service

import org.koin.core.annotation.Single
import ukidelly.api.v1.trade_post.comment.service.TradePostCommentService
import ukidelly.api.v1.trade_post.models.CreateTradePostRequest
import ukidelly.api.v1.trade_post.models.LatestTradePostDto
import ukidelly.api.v1.trade_post.models.TradePostDetail
import ukidelly.api.v1.trade_post.models.TradePostDetailDto
import ukidelly.api.v1.trade_post.repository.TradePostRepository
import java.util.*


@Single
class TradePostService(
    private val tradePostRepository: TradePostRepository,
    private val tradePostCommentService: TradePostCommentService
) {


    suspend fun getLatestPosts(itemsPerPage: Int, page: Int): LatestTradePostDto {
        val result = tradePostRepository.findLatestPosts(itemsPerPage, page)

        return LatestTradePostDto(result.first, page, result.second)

//        return LatestTradePostDto(latestPosts.first, page, latestPosts.second)
    }

    suspend fun getPost(postId: Int): TradePostDetailDto? {
        val tradePostEntity = tradePostRepository.findPost(postId) ?: return null
//        val comments = tradePostCommentService.getAllComment(postId)
        return TradePostDetailDto(TradePostDetail(tradePostEntity), emptyList())
    }

    suspend fun addNewPost(newPost: CreateTradePostRequest, userId: UUID): TradePostDetailDto? {
        val newPostId = tradePostRepository.addNewPost(newPost, userId)
        return getPost(newPostId)
    }

    suspend fun deletePost(postId: Int) {
        tradePostRepository.deletePost(postId)
    }
}