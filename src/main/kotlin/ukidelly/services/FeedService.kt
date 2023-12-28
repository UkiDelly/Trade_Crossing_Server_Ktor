package ukidelly.services

import org.koin.core.annotation.Single
import ukidelly.dto.responses.LatestFeedDto
import ukidelly.models.Feed
import ukidelly.repositories.FeedRepository


@Single
class FeedService(private val feedRepository: FeedRepository) {
    suspend fun getLatestPosts(page: Int, size: Int): LatestFeedDto {
        val feeds = feedRepository.findLatestFeed(size, page)
        return LatestFeedDto(page, size, feeds.first, feeds.second)
    }

    suspend fun getFeedById(feedId: Int): Feed = feedRepository.findFeedById(feedId)
}