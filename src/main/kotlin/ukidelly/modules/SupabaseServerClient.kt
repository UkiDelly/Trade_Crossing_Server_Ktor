package ukidelly.modules

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.http.content.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory


@Single
class SupabaseServerClient {

    private val client = createSupabaseClient(
        supabaseUrl = "https://lvaexmyxiqioemcdupgu.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx2YWV4bXl4aXFpb2VtY2R1cGd1Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTY5MzcxODgwNSwiZXhwIjoyMDA5Mjk0ODA1fQ.6DfxsoXC8eCLCjWOcHNpVusiswjbLsf2GDddfKPI3ek"
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