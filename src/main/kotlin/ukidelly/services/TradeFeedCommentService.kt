package ukidelly.services

import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.dto.responses.TradeFeedCommentDto
import ukidelly.repositories.TradeFeedCommentRepository
import java.util.*


@Single
class TradeFeedCommentService(private val tradeFeedCommentRepository: TradeFeedCommentRepository) {

    private val logger = LoggerFactory.getLogger("CommentService")

    suspend fun getAllComment(postId: Int): List<TradeFeedCommentDto> {

        // Repository에서 모든 댓글 조회
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

            // 해당 부모댓글의 Dto 생성
            val tradeFeedCommentDto = TradeFeedCommentDto(
                tradeFeedComment = parentComment,
                childComments = childTradeFeedCommentDtos
            )

            // 생성한 Dto를 List에 추가
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