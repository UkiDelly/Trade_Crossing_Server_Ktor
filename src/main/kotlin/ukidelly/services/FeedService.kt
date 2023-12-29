package ukidelly.services

import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ukidelly.dto.responses.LatestFeedDto
import ukidelly.models.Feed
import ukidelly.modules.SupabaseServerClient
import ukidelly.repositories.FeedRepository
import java.util.*


@Single
class FeedService(private val feedRepository: FeedRepository) {

    val logger: Logger = LoggerFactory.getLogger(FeedService::class.java)
    private val supabaseClient by inject<SupabaseServerClient>(clazz = SupabaseServerClient::class.java)


    suspend fun getLatestPosts(page: Int, size: Int): LatestFeedDto {
        val (totalPage, feeds) = feedRepository.findLatestFeed(size, page)
        return LatestFeedDto(page, size, totalPage, feeds)
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


    suspend fun updateFeed(
        feedId: Int,
        newImages: List<PartData.FileItem>,
        oldImages: List<Int>,
        content: String?
    ): Feed {
        val oldImagesUrl = feedRepository.deleteImages(oldImages)
        logger.debug("oldImagesUrl: {}", oldImagesUrl)
        supabaseClient.deleteImage(oldImagesUrl)

        if (newImages.isEmpty()) return feedRepository.updateFeed(feedId, content, emptyList())

        val newImageUrls = runBlocking(Dispatchers.IO) {
            newImages.map { async { supabaseClient.uploadImage(it) } }.awaitAll()
        }

        return feedRepository.updateFeed(feedId, content, newImageUrls)
    }
}