package ukidelly.api.v1.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.api.v1.user.service.UserService
import ukidelly.systems.models.ErrorResponseDto
import ukidelly.systems.models.SuccessResponseDto

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



        call.respond(
            HttpStatusCode.OK,
            SuccessResponseDto(data = user, message = "로그인 성공")
        )
    }


    post("/register") {
        val registerRequest = call.receive<UserRegisterRequest>()


        val user = withContext(Dispatchers.IO) {
            service.login(UserLoginRequest(snsId = registerRequest.snsId, email = registerRequest.email))

        }

        if (user != null) {
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponseDto(error = "이미 가입된 유저입니다.", message = "회원가입에 실패 했습니다.")
            )
            return@post
        }

        val newUser = withContext(Dispatchers.IO) {
            service.register(registerRequest)
        }



        call.respond(
            HttpStatusCode.OK,
            SuccessResponseDto(data = "", message = "회원가입 성공")
        )

    }

    authenticate("refresh-jwt") {

    }

    get("/refresh") {
        call.respond(HttpStatusCode.OK, SuccessResponseDto(data = "test", message = "테스트"))
    }


}