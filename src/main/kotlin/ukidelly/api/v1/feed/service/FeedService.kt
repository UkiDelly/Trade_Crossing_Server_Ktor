package ukidelly.api.v1.feed.service

import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject
import ukidelly.api.v1.feed.repository.FeedRepository


@Module
class FeedService {

    private val feedRepository by inject<FeedRepository>(clazz = FeedRepository::class.java)


    fun getLatestPosts(
        page: Int,
        size: Int
    ) {

    }
}