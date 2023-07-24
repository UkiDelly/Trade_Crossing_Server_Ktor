package ukidelly.user.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.systems.domain.ErrorResponseDto
import ukidelly.systems.domain.SuccessResponseDto
import ukidelly.user.domain.UserLoginRequest
import ukidelly.user.domain.UserRegisterRequest
import ukidelly.user.service.UserService

fun Route.userRouting() {

    val logger = LoggerFactory.getLogger("UserRouting")


    val service by inject<UserService>()


    get("/login") {

        val request = call.receive<UserLoginRequest>()

        val user = withContext(Dispatchers.IO) {
            service.login(request)
        }

        // 유저 정보가 없으면 404 에러를 반환
        if (user == null) {
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponseDto(error = "가입 되지 않은 유저입니다.", message = "로그인에 실패 했습니다.")
            )
            return@get
        }




        logger.debug("user: {}", user)
        call.respond(HttpStatusCode.OK, SuccessResponseDto(data = user.properties, message = "로그인 성공"))
    }


    post("/register") {
        val registerRequest = call.receive<UserRegisterRequest>()


        val user = withContext(Dispatchers.IO) {
            service.register(registerRequest)
        }

        if (user == null) {
            call.respond(HttpStatusCode.Conflict, ErrorResponseDto(error = "이미 가입된 유저입니다.", message = "회원가입에 실패 했습니다."))
            return@post
        }

        call.respond(HttpStatusCode.Created, SuccessResponseDto(data = user.properties, message = "회원가입 성공"))

    }


}