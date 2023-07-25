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
import ukidelly.api.v1.user.models.UserLoginRequest
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.api.v1.user.models.UserResponse
import ukidelly.api.v1.user.service.UserService
import ukidelly.systems.models.ErrorResponseDto
import ukidelly.systems.models.SuccessResponseDto
import ukidelly.systems.models.Token

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

        val token = Token.createToken(application.environment.config, user.userId.toString())



        call.respond(
            HttpStatusCode.OK,
            SuccessResponseDto(UserResponse(user, token), message = "로그인 성공")
        )
    }


    post("/register") {
        val registerRequest = call.receive<UserRegisterRequest>()


        val newUser = withContext(Dispatchers.IO) {
            service.register(registerRequest)
        } ?: run {
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponseDto(error = "이미 가입된 유저입니다.", message = "회원가입에 실패 했습니다.")
            )
            return@post
        }

        val token = Token.createToken(application.environment.config, newUser.userId.toString())


        call.respond(
            HttpStatusCode.OK,
            SuccessResponseDto(UserResponse(newUser, token), message = "회원가입 성공")
        )

    }

    authenticate("refresh-jwt") {

        get("/refresh") {
            call.respond(HttpStatusCode.OK, SuccessResponseDto(data = "test", message = "테스트"))
        }
    }


}