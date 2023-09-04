@file:Suppress("UNCHECKED_CAST")

package ukidelly.api.v1.image

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.http.content.PartData.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.modules.SupabaseServerClient
import ukidelly.systems.models.ResponseDto

fun Route.imageRouting() {

    val logger = LoggerFactory.getLogger("ImageRouting")
    val supabaseClient by inject<SupabaseServerClient>()

    authenticate("auth-jwt") {
        post("/upload") {


            val userId = call.principal<UserIdPrincipal>()!!.name
            val imageUrlMap = mutableMapOf<Int, String>()
            val imageFiles = call.receiveMultipart().readAllParts() as List<FileItem>

            imageFiles.forEachIndexed { index, fileItem ->
                val url = supabaseClient.uploadImage(userId, fileItem)
                imageUrlMap[index] = url
            }

            call.respond(HttpStatusCode.OK, ResponseDto.Success(data = imageUrlMap, message = "성공"))
        }
    }
}