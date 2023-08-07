package ukidelly.api.v1.user

import io.ktor.http.*
import io.ktor.server.application.*
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
import ukidelly.systems.models.LoginType
import ukidelly.systems.models.ResponseDto
import ukidelly.systems.models.Token

fun Route.userRouting() {

    val logger = LoggerFactory.getLogger("UserRouting")


    val service by inject<UserService>()


    route("/login") {

        get("/kakao") {

            val loginType = LoginType.KAKAO

            val request = call.receive<UserLoginRequest>()

            service.login(snsId = request.snsId, email = request.email, loginType = loginType).let { user ->

                if (user == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ResponseDto.Error(error = "가입 되지 않은 유저입니다.", message = "로그인에 실패 했습니다.")
                    )
                    return@get
                } else {
                    val token = Token.createToken(application.environment.config, user.userId.toString())
                    call.respond(
                        HttpStatusCode.OK,
                        ResponseDto.Success(data = UserResponse(user, token), message = "로그인 성공")
                    )
                    return@get
                }

            }


        }

        get("/google") {

            val loginType = LoginType.GOOGLE

            val request = call.receive<UserLoginRequest>()

            val user = withContext(Dispatchers.IO) {
                service.login(
                    snsId = request.snsId,
                    email = request.email,
                    loginType = loginType
                )
            }

            if (user == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    ResponseDto.Error(error = "가입 되지 않은 유저입니다.", message = "로그인에 실패 했습니다.")
                )
            } else {
                val token = Token.createToken(application.environment.config, user.userId.toString())
                call.respond(
                    HttpStatusCode.OK,
                    ResponseDto.Success(data = UserResponse(user, token), message = "로그인 성공")
                )
            }
        }

        get("/apple") {
            val loginType = LoginType.APPLE
            val request = call.receive<UserLoginRequest>()
            val user = withContext(Dispatchers.IO) {
                service.login(
                    snsId = request.snsId,
                    email = request.email,
                    loginType = loginType
                )
            }

            if (user == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    ResponseDto.Error(error = "가입 되지 않은 유저입니다.", message = "로그인에 실패 했습니다.")
                )
            } else {
                val token = Token.createToken(application.environment.config, user.userId.toString())
                call.respond(
                    HttpStatusCode.OK,
                    ResponseDto.Success(data = UserResponse(user, token), message = "로그인 성공")
                )
            }
        }
    }


    post("/register") {

        val request = call.receive<UserRegisterRequest>()
        service.register(request)?.let {
            val token = Token.createToken(application.environment.config, it.userId.toString())
            call.respond(HttpStatusCode.OK, ResponseDto.Success(UserResponse(it, token), message = "회원가입 성공"))
        } ?: run {
            call.respond(HttpStatusCode.Conflict, ResponseDto.Error("이미 가입한 유저입니다.", message = "회원가입 실패"))
        }


    }

}