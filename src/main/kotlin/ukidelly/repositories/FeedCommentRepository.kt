package ukidelly.repositories

import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory


@Single
class FeedCommentRepository {

  private val logger = LoggerFactory.getLogger(this::class.java)
}