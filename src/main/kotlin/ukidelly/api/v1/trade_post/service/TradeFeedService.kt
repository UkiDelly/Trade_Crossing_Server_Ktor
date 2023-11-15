package ukidelly.api.v1.trade_post.service

import org.koin.core.annotation.Single
import ukidelly.api.v1.trade_post.comment.service.TradeFeedCommentService
import ukidelly.api.v1.trade_post.models.CreateTradeFeedRequest
import ukidelly.api.v1.trade_post.models.LatestTradeFeedDto
import ukidelly.api.v1.trade_post.models.TradeFeedDto
import ukidelly.api.v1.trade_post.repository.TradeFeedRepository
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
        val tradeFeedEntity = tradeFeedRepository.findPost(postId)
        val commentList = tradeFeedCommentService.getAllComment(postId)
        return TradeFeedDto(tradeFeedEntity, commentList)
    }

    suspend fun addNewPost(newPost: CreateTradeFeedRequest, userId: UUID): TradeFeedDto {
        val newPostId = tradeFeedRepository.addNewPost(newPost, userId)
        return getPost(newPostId)
    }

    suspend fun deletePost(postId: Int) {
        tradeFeedRepository.deletePost(postId)
    }
}