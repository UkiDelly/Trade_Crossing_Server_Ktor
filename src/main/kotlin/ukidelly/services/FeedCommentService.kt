package ukidelly.services

import org.koin.core.annotation.Single
import ukidelly.repositories.FeedCommentRepository


@Single
class FeedCommentService(private val feedCommentRepository: FeedCommentRepository)