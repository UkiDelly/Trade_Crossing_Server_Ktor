package ukidelly.api.v1.trade_post.service

import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent.inject
import ukidelly.api.v1.trade_post.comment.service.TradePostCommentService
import ukidelly.api.v1.trade_post.models.TradePostCreateRequest
import ukidelly.api.v1.trade_post.models.dto.LatestTradePostDto
import ukidelly.api.v1.trade_post.models.dto.TradePostDetailDto
import ukidelly.api.v1.trade_post.repository.TradePostRepository


@Single
class TradePostService(
    private val tradePostRepository: TradePostRepository,
    private val tradePostCommentService: TradePostCommentService
) {


    suspend fun getLatestPosts(itemsPerPage: Int, page: Int): LatestTradePostDto {
        val latestPosts = tradePostRepository.findLatestPosts(itemsPerPage, page)
        return LatestTradePostDto(latestPosts.first, page, latestPosts.second)
    }

    suspend fun getPost(postId: Int): TradePostDetailDto? {
        val post = tradePostRepository.findPost(postId) ?: return null
        val comments = tradePostCommentService.getAllComment(postId)
        return TradePostDetailDto(post, comments)
    }

    suspend fun addNewPost(newPost: TradePostCreateRequest, userId: Int): TradePostDetailDto? {
        val newPostId = tradePostRepository.addNewPost(newPost, userId)
        return getPost(newPostId.value)
    }

    suspend fun deletePost(postId: Int) {
        tradePostRepository.deletePost(postId)
    }
}