package ukidelly.api.v1.trade_post.comment.service

import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import ukidelly.api.v1.trade_post.comment.models.TradeFeedComment
import ukidelly.api.v1.trade_post.comment.models.TradeFeedCommentDto
import ukidelly.database.models.comment.TradeFeedCommentRepository
import ukidelly.database.models.comment.TradeFeedComments


@Single
class TradeFeedCommentService(private val tradeFeedCommentRepository: TradeFeedCommentRepository) {


    private val logger = LoggerFactory.getLogger("CommentService")

    suspend fun getCommentCounts(postIdList: List<Int>): List<Long> {
        return tradeFeedCommentRepository.getCommentCount(postIdList)
    }

    suspend fun getAllComment(postId: Int): List<TradeFeedCommentDto> {

        // Repository에서 ResultRow형태의 List를 받음
        val comments = tradeFeedCommentRepository.findAllComments(postId)

        // Routing에 전달할 빈 List<CommentDto> 생성
        val tradeFeedCommentDtos = mutableListOf<TradeFeedCommentDto>()

        // 부모 댓글 분리
        val parentTradeFeedComments = comments.filter {
            it[TradeFeedComments.parentCommentId] == null
        }.map {
            TradeFeedComment(it)
        }

        // 대댓글 분리
        val childTradeFeedComments = comments.filter {
            it[TradeFeedComments.parentCommentId] != null
        }.map { TradeFeedComment(it) }

        // 부모 댓글 for-loop
        for (comment in parentTradeFeedComments) {

            // 새로운 childComments가 빈배열인 CommentDto 생성
            var tradeFeedCommentDto = TradeFeedCommentDto(
                tradeFeedComment = comment,
                childComments = emptyList()
            )

            // 부모댓글의 대댓글들을 담기 위한 List를 생성
            var childTradeFeedCommentDtos = mutableListOf<TradeFeedCommentDto>()

            // 분리한 대댓글 리스트에서 for-loop
            for (childComment in childTradeFeedComments) {

                // 대댓글의 parentCommentId가 부모댓글의 commentId와 일치한지 확인
                if (comment.commentId == childComment.parentCommentId) {

                    // 일치하면 새로운 CommentDto를 생성하여 childCommentDto에 담기
                    childTradeFeedCommentDtos.add(
                        TradeFeedCommentDto(
                            tradeFeedComment = childComment,
                            childComments = emptyList()
                        )
                    )
                }
            }

            // 대댓글의 요소들을 sort해서 다시 담기
            childTradeFeedCommentDtos.sortBy { it.createdAt }

            // 부모 댓글 Dto의 childComments에 sort된 childComments을 업데이트
            tradeFeedCommentDto.childComments = childTradeFeedCommentDtos
            // 전체의 commentDtos에 새로운 CommentDto를 추가
            tradeFeedCommentDtos.add(tradeFeedCommentDto)
        }
        logger.debug("comments : {}", tradeFeedCommentDtos)
        return tradeFeedCommentDtos
    }


}