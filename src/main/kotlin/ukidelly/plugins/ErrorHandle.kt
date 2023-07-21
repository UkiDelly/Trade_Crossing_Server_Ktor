package ukidelly.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException
import org.slf4j.LoggerFactory
import ukidelly.domain.system.ErrorResponseModel
import ukidelly.utils.toSnakeCase

@OptIn(ExperimentalSerializationApi::class)
fun Application.errorHandle() {
    install(StatusPages) {

        val logger = LoggerFactory.getLogger("ErrorHandle")


        //
        exception<IllegalArgumentException> { call, exception ->
            logger.error(exception.cause.toString())
            call.respond(HttpStatusCode.BadRequest, exception.message!!)
        }


        // 잘못된 요청이 들어왔을 때
        exception<BadRequestException> { call, exception ->

            when {

                // 필드가 누락되었을 때
                findException<MissingFieldException>(exception) != null -> {
                    val cause = findException<MissingFieldException>(exception)!!
                    val responseModel = ErrorResponseModel(
                        error = cause.missingFields.let {
                            it.map { field -> field.toSnakeCase() }

                        },
                        message = "해당 필드가 누락되었습니다."
                    )

                    call.respond(HttpStatusCode.BadRequest, responseModel)
                }

                // 쿼리 파라미터가 누락되었을 때
                findException<MissingRequestParameterException>(exception) != null -> {
                    val cause = findException<MissingRequestParameterException>(exception)!!
                    val responseModel = ErrorResponseModel(
                        error = cause.parameterName,
                        message = "해당 쿼리 파라미터가 누락되었습니다."
                    )
                    call.respond(HttpStatusCode.BadRequest, responseModel)
                }

                //
                findException<SerializationException>(exception) != null -> {
                    val cause = findException<SerializationException>(exception)!!

                    // find the word between '' in the message
                    val regex = Regex("'(.*?)'")
                    val matchResult = regex.find(cause.message!!)!!
                    val responseModel = ErrorResponseModel(
                        error = matchResult.value,
                        message = "잘못된 값입니다"
                    )
                    call.respond(HttpStatusCode.BadRequest, responseModel)
                }

                findException<IllegalArgumentException>(exception) != null -> {
                    val cause = findException<IllegalArgumentException>(exception)!!
                    val responseModel = ErrorResponseModel(
                        error = null,
                        message = cause.message!!
                    )
                    call.respond(HttpStatusCode.Unauthorized, responseModel)
                }


                // 잘못된 요청이 들어왔을 때
                else -> call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponseModel(error = null, message = "잘못된 요청입니다.")
                )
            }

        }


    }

}

/**
 * 입력된 exception에 [T] 타입의 exception이 있는지 확인하는 함수입니다.
 * @param exception 확인할 exception
 * @return [T] 타입의 exception이 있으면 해당 exception을 반환하고 없으면 null을 반환합니다.
 */
private inline fun <reified T> findException(exception: Throwable): T? {
    return generateSequence(exception) { it.cause }.filterIsInstance<T>()
        .firstOrNull()
}