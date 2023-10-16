package ukidelly.api.v1.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.api.v1.user.models.*
import ukidelly.api.v1.user.service.UserService
import ukidelly.modules.getToken
import ukidelly.systems.errors.ServerError
import ukidelly.systems.models.LoginType
import ukidelly.systems.models.ResponseDto
import ukidelly.systems.models.Token
import java.util.*

fun Route.userRouting() {

    val logger = LoggerFactory.getLogger("UserRouting")
    val service by inject<UserService>()
    route("/login") {

        post("/email") {
            val loginType = LoginType.email
            val request = call.receive<EmailLoginReqeust>()

            val user = service.emailLogin(request.email, request.password)

            chechUserAndRespond(user, call, application.environment.config)
        }

        post("/kakao") {

            val loginType = LoginType.kakao
            val request = call.receive<SocialLoginRequest>()
            service.socialLogin(snsId = request.snsId, email = request.email, loginType = loginType).let { user ->
                if (user == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ResponseDto.Error(error = "가입 되지 않은 유저입니다.", message = "로그인에 실패 했습니다.")
                    )
                    return@post
                } else {
                    val token = Token.createToken(application.environment.config, user.uuid.toString())
                    call.respond(
                        HttpStatusCode.OK,
                        ResponseDto.Success(data = UserResponse(user, token), message = "로그인 성공")
                    )
                    return@post
                }
            }
        }

        post("/google") {

            val loginType = LoginType.google
            val request = call.receive<SocialLoginRequest>()
            val user = withContext(Dispatchers.IO) {
                service.socialLogin(
                    snsId = request.snsId,
                    email = request.email,
                    loginType = loginType
                )
            }

            chechUserAndRespond(user, call, application.environment.config)
        }

        post("/apple") {
            val loginType = LoginType.apple
            val request = call.receive<SocialLoginRequest>()
            val user = withContext(Dispatchers.IO) {
                service.socialLogin(
                    snsId = request.snsId,
                    email = request.email,
                    loginType = loginType
                )
            }

            chechUserAndRespond(user, call, application.environment.config)
        }

        authenticate("refresh-jwt") {

            post("/refresh") {
                val token = call.getToken()
                call.respond(HttpStatusCode.OK, ResponseDto.Success(data = token, "재발급에 성공하였습니다."))
            }

            post("/auto") {
                val uuid = call.principal<UserIdPrincipal>()!!.name
                val user = withContext(Dispatchers.IO) {
                    service.autoLogin(UUID.fromString(uuid))
                }

                if (user == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ResponseDto.Error(error = "가입 되지 않은 유저입니다.", message = "로그인에 실패 했습니다.")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        ResponseDto.Success(data = UserResponse(user, call.getToken()), message = "성공")
                    )
                }
            }
        }
    }

    post("/register") {

        val request = call.receive<UserRegisterRequest>()
        service.register(request).let {
            val token = Token.createToken(application.environment.config, it.uuid.toString())
            call.respond(HttpStatusCode.OK, ResponseDto.Success(UserResponse(it, token), message = "회원가입 성공"))
        }

    }

}


/**
 * 유저 null 체크 후 응답
 * @param [User]? 유저정보
 * @param call [ApplicationCall]
 * @param config [ApplicationConfig]
 */
private suspend fun chechUserAndRespond(user: User?, call: ApplicationCall, config: ApplicationConfig) {
    if (user == null) {
        call.respond(
            HttpStatusCode.NotFound,
            ResponseDto.Error(error = ServerError.NotExist, message = "존재하지 않는 유저입니다.")
        )
    } else {
        val token = Token.createToken(config, user.uuid.toString())
        call.respond(
            HttpStatusCode.OK,
            ResponseDto.Success(data = UserResponse(user, token), message = "로그인 성공")
        )
    }
}