package ukidelly.modules

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.http.content.*
import io.ktor.server.config.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory


@Single
class SupabaseServerClient(config: ApplicationConfig) {

    private val client = createSupabaseClient(
        supabaseUrl = System.getenv("supabase_url") ?: config.property("supabase.url").getString(),
        supabaseKey = System.getenv("supabase_key") ?: config.property("supabase.key").getString(),
    ) { install(Storage) }

    private val bucketName = "images"
    private val bucket = client.storage.from(bucketName)

    private val logger = LoggerFactory.getLogger("Supabase Client")


    suspend fun uploadImage(file: PartData.FileItem): String {

        val fileByteArray = file.streamProvider().readBytes()
        val imagePath = file.originalFileName!!
        withContext(Dispatchers.IO) {
            async { bucket.upload(path = imagePath, fileByteArray, upsert = false) }.await()
        }
        
        return bucket.publicUrl(imagePath)
    }

    suspend fun deleteImage(imagePath: String) {
        CoroutineScope(Dispatchers.IO).async {
            bucket.delete(imagePath)
        }.await()
    }

    suspend fun listBuckets() {

        withContext(Dispatchers.IO) {
            val logger = LoggerFactory.getLogger("Supabase Client")
            val buckets = client.storage.retrieveBuckets()

            logger.debug("buckets: {}", buckets)
        }
    }
}