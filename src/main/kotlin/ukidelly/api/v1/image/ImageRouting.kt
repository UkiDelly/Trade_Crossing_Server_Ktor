package ukidelly.api.v1.image

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.modules.SupabaseClient

fun Route.imageRouting() {

    val logger = LoggerFactory.getLogger("ImageRouting")
    val supabaseClient by inject<SupabaseClient>()

    authenticate("auth-jwt") {
        post("/upload") {

            val imageFile = call.receiveMultipart()
            val keyName = imageFile.readPart() as PartData.FileItem

            logger.debug("keyName: ${keyName.originalFileName}")
            call.respond(HttpStatusCode.OK, "success")

            supabaseClient.listBuckets()

            TODO("S3를 연결한 다음 S3에 파일 업로드")
        }
    }
}