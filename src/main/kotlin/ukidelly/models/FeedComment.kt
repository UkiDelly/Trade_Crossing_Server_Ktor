package ukidelly.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class FeedComment(
  val id: Int,
  val content: String,
  val creator: CompactUser,
  val parentComment: Int?,
  @Contextual
  val createdAt: LocalDateTime,
  @Contextual
  val updatedAt: LocalDateTime
)
