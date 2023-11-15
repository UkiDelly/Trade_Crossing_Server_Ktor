package ukidelly.api.v1.feed.service

import org.koin.core.annotation.Single
import ukidelly.api.v1.feed.models.FeedPreviewModel
import ukidelly.api.v1.feed.models.NewFeedDto
import ukidelly.api.v1.feed.repository.FeedRepository


@Single
class FeedService(private val feedRepository: FeedRepository) {


    suspend fun getLatestPosts(
        page: Int,
        size: Int
    ): List<FeedPreviewModel> {
        return feedRepository.findLatestFeed(size, page)

    }


    suspend fun addNewFeed(userId: Int, newFeed: NewFeedDto) {

        feedRepository.addNewFeed(userId, newFeed.content, newFeed.images)

    }
}