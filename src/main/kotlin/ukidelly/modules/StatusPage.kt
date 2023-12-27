package ukidelly.modules

import com.auth0.jwt.exceptions.TokenExpiredException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.slf4j.LoggerFactory
import ukidelly.dto.responses.ResponseDto
import ukidelly.systems.errors.InvalidJwtTokenException
import ukidelly.systems.errors.PasswordIncorrectException
import ukidelly.systems.errors.ServerError
import ukidelly.systems.errors.UserExistException


@OptIn(ExperimentalSerializationApi::class)
fun Application.configureStatusPage() {
    install(StatusPages) {
        val logger = LoggerFactory.getLogger("ErrorHandle")

        // 존재하지 않음
        exception<NotFoundException> { call, exception ->
            call.respond(HttpStatusCode.NotFound, ResponseDto.Error(ServerError.NotExist, exception.message ?: "실패"))
        }

        //
        exception<InvalidJwtTokenException> { call, exception ->
            logger.error(exception.cause.toString())
            call.respond(
                HttpStatusCode.Unauthorized,
                ResponseDto.Error(error = ServerError.UnAuhtorized, message = "유효하지 않은 토큰입니다.")
            )
        }

        //
        exception<TokenExpiredException> { call, exception ->
            logger.error(exception.cause.toString())
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseDto.Error(error = ServerError.UnAuhtorized, message = "유효하지 않은 토큰입니다.")
            )
        }
        // Request의 Body에서 필드가 누락되었을 때
        exception<RequestValidationException> { call, exception ->
            call.respond(
                HttpStatusCode.NotAcceptable,
                ResponseDto.Error(error = ServerError.InvalidField, message = "${exception.reasons}가 유효하지 않습니다.")
            )
        }
        exception<BadRequestException> { call, exception ->
            logger.error(exception.cause.toString())
            findException<NullPointerException>(exception)?.let {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ResponseDto.Error(error = ServerError.Null, message = "null 값이 입력되었습니다.")
                )
            }
        }
        exception<MissingFieldException> { call, exception ->
            call.respond(
                HttpStatusCode.BadRequest,
                ResponseDto.Error(error = ServerError.MissingField, message = "${exception.missingFields} 필드가 누락되었습니다.")
            )
        }

        // 커스텀 에러: 유저가 이미 존재
        exception<UserExistException> { call, e ->
            call.respond(HttpStatusCode.Conflict, ResponseDto.Error(ServerError.UserExist, e.message!!))
        }

        // 커스턴 에러: 비밀번호 맞지 않음
        exception<PasswordIncorrectException> { call, e ->
            call.respond(HttpStatusCode.Forbidden, ResponseDto.Error(ServerError.PasswordIncorrect, e.message!!))
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