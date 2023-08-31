package ukidelly.api.v1.trade_post.comment.service

import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory
import ukidelly.api.v1.trade_post.comment.models.TradePostComment
import ukidelly.api.v1.trade_post.comment.models.TradePostCommentDto
import ukidelly.database.models.comment.TradePostCommentRepository
import ukidelly.database.models.comment.TradePostCommentTable


@Module
class TradePostCommentService {

    val tradePostCommentRepository by inject<TradePostCommentRepository>(clazz = TradePostCommentRepository::class.java)
    val logger = LoggerFactory.getLogger("CommentService")

    suspend fun getAllComment(postId: Int): List<TradePostCommentDto> {

        // Repository에서 ResultRow형태의 List를 받음
        val comments = tradePostCommentRepository.findAllComments(postId)

        // Routing에 전달할 빈 List<CommentDto> 생성
        val tradePostCommentDtos = mutableListOf<TradePostCommentDto>()

        // 부모 댓글 분리
        val parentTradePostComments = comments.filter {
            it[TradePostCommentTable.parentCommentId] == null
        }.map {
            TradePostComment.fromRow(it)
        }

        // 대댓글 분리
        val childTradePostComments = comments.filter {
            it[TradePostCommentTable.parentCommentId] != null
        }.map { TradePostComment.fromRow(it) }

        // 부모 댓글 for-loop
        for (comment in parentTradePostComments) {

            // 새로운 childComments가 빈배열인 CommentDto 생성
            var tradePostCommentDto: TradePostCommentDto = TradePostCommentDto.fromComment(
                tradePostComment = comment,
                childComments = emptyList()
            )

            // 부모댓글의 대댓글들을 담기 위한 List를 생성
            var childTradePostCommentDtos = mutableListOf<TradePostCommentDto>()

            // 분리한 대댓글 리스트에서 for-loop
            for (childComment in childTradePostComments) {

                // 대댓글의 parentCommentId가 부모댓글의 commentId와 일치한지 확인
                if (comment.commentId == childComment.parentCommentId) {

                    // 일치하면 새로운 CommentDto를 생성하여 childCommentDto에 담기
                    childTradePostCommentDtos.add(
                        TradePostCommentDto.fromComment(
                            tradePostComment = childComment,
                            childComments = emptyList()
                        )
                    )
                }
            }

            // 대댓글의 요소들을 sort해서 다시 담기
            childTradePostCommentDtos = childTradePostCommentDtos.sortedBy { it.createdAt }.toMutableList()

            // 부모 댓글 Dto의 childComments에 sort된 childComments을 업데이트
            tradePostCommentDto = tradePostCommentDto.copy(childComments = childTradePostCommentDtos)
            // 전체의 commentDtos에 새로운 CommentDto를 추가
            tradePostCommentDtos.add(tradePostCommentDto)
        }
        logger.debug("comments : {}", tradePostCommentDtos)
        return tradePostCommentDtos
    }


}