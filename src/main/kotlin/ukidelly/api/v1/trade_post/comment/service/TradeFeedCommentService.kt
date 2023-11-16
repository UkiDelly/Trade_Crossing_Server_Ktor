package ukidelly.api.v1.trade_post.comment.service

import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.api.v1.trade_post.comment.models.TradeFeedCommentDto
import ukidelly.database.models.comment.TradeFeedCommentRepository
import java.util.*


@Single
class TradeFeedCommentService(private val tradeFeedCommentRepository: TradeFeedCommentRepository) {

    private val logger = LoggerFactory.getLogger("CommentService")

    suspend fun getAllComment(postId: Int): List<TradeFeedCommentDto> {

        // Repository에서 ResultRow형태의 List를 받음
        val comments = tradeFeedCommentRepository.findAllComments(postId)
        // Routing에 전달할 빈 List<CommentDto> 생성
        val tradeFeedCommentDtoList = mutableListOf<TradeFeedCommentDto>()
        // 부모 댓글 분리
        val parentComments = comments.filter { it.parentCommentId == null }
        // 대댓글 분리
        val childComments = comments.filter { it.parentCommentId != null }
        // 부모 댓글 for-loop
        parentComments.forEach { parentComment ->
            // 대댓글 찾기
            val childTradeFeedCommentDtos = childComments.filter { it.parentCommentId == parentComment.commentId }
                .map { TradeFeedCommentDto(it, emptyList()) }

            // 새로운 childComments가 빈배열인 CommentDto 생성
            val tradeFeedCommentDto = TradeFeedCommentDto(
                tradeFeedComment = parentComment,
                childComments = childTradeFeedCommentDtos
            )

            tradeFeedCommentDtoList.add(tradeFeedCommentDto)
        }
        return tradeFeedCommentDtoList
    }


    suspend fun addNewComment(
        postId: Int,
        content: String,
        uuid: UUID,
        reply: Boolean = false,
        parentCommentId: Int? = null
    ) =
        tradeFeedCommentRepository.addNewComment(postId, content, uuid, reply, parentCommentId)

    suspend fun updateComment(commentId: Int, content: String) =
        tradeFeedCommentRepository.updateComment(commentId, content)

    suspend fun deleteComment(commentId: Int) = tradeFeedCommentRepository.deleteComment(commentId)
}