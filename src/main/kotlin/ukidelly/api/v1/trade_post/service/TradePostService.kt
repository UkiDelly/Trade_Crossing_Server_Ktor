package ukidelly.api.v1.trade_post.service

import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject
import ukidelly.api.v1.trade_post.comment.service.TradePostCommentService
import ukidelly.api.v1.trade_post.models.TradePostCreateRequest
import ukidelly.api.v1.trade_post.models.dto.LatestTradePostDto
import ukidelly.api.v1.trade_post.models.dto.TradePostDetailDto
import ukidelly.api.v1.trade_post.repository.TradePostRepository
import java.util.*


@Module
class TradePostService {

    private val tradePostRepository by inject<TradePostRepository>(clazz = TradePostRepository::class.java)
    private val tradePostCommentService by inject<TradePostCommentService>(clazz = TradePostCommentService::class.java)

    suspend fun getLatestPosts(itemsPerPage: Int, page: Int): LatestTradePostDto {
        val latestPosts = tradePostRepository.findLatestPosts(itemsPerPage, page)
        return LatestTradePostDto(latestPosts.first, page, latestPosts.second)
    }

    suspend fun getPost(postId: Int): TradePostDetailDto? {
        val post = tradePostRepository.findPost(postId) ?: return null
        val comments = tradePostCommentService.getAllComment(postId)
        return TradePostDetailDto(post, comments)
    }

    suspend fun addNewPost(newPost: TradePostCreateRequest, userId: UUID): TradePostDetailDto? {
        val newPostId = tradePostRepository.addNewPost(newPost, userId)
        return getPost(newPostId.value)
    }

    suspend fun deletePost(postId: Int) {
        tradePostRepository.deletePost(postId)
    }
}