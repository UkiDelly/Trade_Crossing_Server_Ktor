@file:Suppress("UNCHECKED_CAST")

package ukidelly.api.v2.image

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.http.content.PartData.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.dto.responses.ResponseDto.Success
import ukidelly.modules.SupabaseServerClient

fun Route.imageRouting() {

    val logger = LoggerFactory.getLogger("ImageRouting")
    val supabaseClient by inject<SupabaseServerClient>()

    authenticate("auth-jwt") {
        post<ImageRoutes.Upload> {


            val userId = call.principal<UserIdPrincipal>()!!.name
            val imageUrlMap = mutableMapOf<Int, String>()
            val imageFiles = call.receiveMultipart().readAllParts() as List<FileItem>

            runBlocking(Dispatchers.IO) {
                val uploadJob = imageFiles.mapIndexed { index, fileItem ->
                    async {
                        val url = supabaseClient.uploadImage(userId, fileItem)
                        logger.debug("index: $index, url: $url")
                        imageUrlMap[index] = url
                    }
                }

                uploadJob.awaitAll()
            }
            call.respond(HttpStatusCode.OK, Success(data = imageUrlMap, message = "성공"))
        }
    }
}