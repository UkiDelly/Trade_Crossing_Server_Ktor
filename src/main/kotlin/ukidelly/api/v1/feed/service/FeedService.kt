package ukidelly.api.v1.feed.service

import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject
import ukidelly.api.v1.feed.models.NewFeedDto
import ukidelly.api.v1.feed.repository.FeedRepository
import java.util.*


@Module
class FeedService {

    private val feedRepository by inject<FeedRepository>(clazz = FeedRepository::class.java)


    suspend fun getLatestPosts(
        page: Int,
        size: Int
    ) {
        feedRepository.findLatestFeed(size, page)
    }


    suspend fun addNewFeed(userId: UUID, newFeed: NewFeedDto) {

        feedRepository.addNewFeed(userId, newFeed.content, newFeed.images)

    }
}