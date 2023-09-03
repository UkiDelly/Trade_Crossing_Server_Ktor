package ukidelly.modules

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Module


@Module
class SupabaseClient {

    private val client = createSupabaseClient(
        supabaseUrl = "https://lvaexmyxiqioemcdupgu.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx2YWV4bXl4aXFpb2VtY2R1cGd1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTM3MTg4MDUsImV4cCI6MjAwOTI5NDgwNX0.-0vVytWcnQFtoCf8IFBq08WDvDlPNonqZHLOcVxytvE"
    ) {}


    suspend fun uploadImage() {
        client.storage.retrieveBuckets()
    }

    suspend fun listBuckets() {

        withContext(Dispatchers.IO) {
            val buckets = client.storage.retrieveBuckets()
            println(buckets)
        }
    }
}