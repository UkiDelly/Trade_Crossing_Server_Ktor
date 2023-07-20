package ukidelly.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.slf4j.LoggerFactory
import ukidelly.domain.system.ResponseModel

@OptIn(ExperimentalSerializationApi::class)
fun Application.errorHandle() {
    install(StatusPages) {
        val logger = LoggerFactory.getLogger("ErrorHandle")
        exception<BadRequestException> { call, exception ->
            when {
                findException<MissingFieldException>(exception) != null -> {
                    val cause = findException<MissingFieldException>(exception)!!
                    val responseModel = ResponseModel(
                        data = cause.missingFields,
                        message = "해당 필드가 누락되었습니다."
                    )

                    call.respond(HttpStatusCode.BadRequest, responseModel)
                }

                findException<MissingRequestParameterException>(exception) != null -> {
                    val cause = findException<MissingRequestParameterException>(exception)!!
                    val responseModel = ResponseModel(
                        data = cause.parameterName,
                        message = "해당 쿼리 파라미터가 누락되었습니다."
                    )
                    call.respond(HttpStatusCode.BadRequest, responseModel)
                }

                else -> call.respond(
                    HttpStatusCode.BadRequest,
                    ResponseModel(data = null, message = "잘못된 요청입니다.")
                )
            }

        }

        
    }

}


private inline fun <reified T> findException(exception: Throwable): T? {
    return generateSequence(exception) { it.cause }.filterIsInstance<T>()
        .firstOrNull()
}