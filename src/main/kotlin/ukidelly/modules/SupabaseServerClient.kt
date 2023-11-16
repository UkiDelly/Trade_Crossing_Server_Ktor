package ukidelly.modules

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.http.content.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory


@Single
class SupabaseServerClient(val config: ApplicationConfig) {

    private val client = createSupabaseClient(
        supabaseUrl = System.getenv("supabase_url") ?: config.property("supabaes.url").getString(),
        supabaseKey = System.getenv("supabase_key") ?: config.property("supabaes.key").getString(),
    ) {
        install(Storage) {
        }
    }

    private val bucket = "images"

    suspend fun uploadImage(userId: String, file: PartData.FileItem): String {

        val fileByteArray = file.streamProvider().readBytes()
        val imagePath = "${userId}/${file.originalFileName!!}"

        withContext(Dispatchers.IO) {
            client.storage.from(bucket)
                .upload(path = imagePath, fileByteArray, upsert = true)
        }
        return client.storage.from(bucket).publicUrl(imagePath)
    }

    suspend fun listBuckets() {

        withContext(Dispatchers.IO) {
            val logger = LoggerFactory.getLogger("Supabase Client")
            val buckets = client.storage.retrieveBuckets()
        }
    }
}