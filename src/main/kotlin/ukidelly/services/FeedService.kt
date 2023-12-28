package ukidelly.services

import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory
import ukidelly.dto.responses.LatestFeedDto
import ukidelly.models.Feed
import ukidelly.modules.SupabaseServerClient
import ukidelly.repositories.FeedRepository
import java.util.*


@Single
class FeedService(private val feedRepository: FeedRepository) {

    val logger = LoggerFactory.getLogger(FeedService::class.java)
    val supabaseClient by inject<SupabaseServerClient>(clazz = SupabaseServerClient::class.java)


    suspend fun getLatestPosts(page: Int, size: Int): LatestFeedDto {
        val feeds = feedRepository.findLatestFeed(size, page)
        return LatestFeedDto(page, size, feeds.first, feeds.second)
    }

    suspend fun getFeedById(feedId: Int): Feed = feedRepository.findFeedById(feedId)

    suspend fun addNewFeed(userUUID: UUID, images: List<PartData.FileItem>, content: String): Feed {
        val imageUrls = mutableListOf<String>()

        runBlocking(Dispatchers.IO) {
            images.map {
                async {
                    val url = supabaseClient.uploadImage(it)
                    imageUrls.add(url)
                }
            }.awaitAll()
        }

        return feedRepository.addNewFeed(userUUID, content, imageUrls)
    }


}