package ukidelly.api.v1.user


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.api.v1.user.models.EmailLoginReqeust
import ukidelly.api.v1.user.models.SocialLoginRequest
import ukidelly.api.v1.user.models.UserInfoWithToken
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.api.v1.user.service.UserService
import ukidelly.modules.getToken
import ukidelly.systems.models.ResponseDto
import ukidelly.systems.models.Token
import java.util.*

fun Route.userRouting() {

    val logger = LoggerFactory.getLogger("UserRouting")
    val service by inject<UserService>()


    // 이메일로 로그인
    post<UserRoutes.Login.EmailLogin> {
        val request = call.receive<EmailLoginReqeust>()

        val user = service.emailLogin(request.email, request.password)
        val token = Token.createToken(application.environment.config, user.uuid.toString())
        call.respond(HttpStatusCode.OK, ResponseDto.Success(UserInfoWithToken(user, token), "성공"))
    }

    // 소셜 로그인
    post<UserRoutes.Login.SocialLogin> {
        val request = call.receive<SocialLoginRequest>()
        val user = service.socialLogin(request.snsId, email = request.email, loginType = request.loginType)
        val token = Token.createToken(application.environment.config, user.uuid.toString())
        call.respond(
            HttpStatusCode.OK,
            ResponseDto.Success(UserInfoWithToken(user = user, token = token), message = "성공")
        )
    }

    // 회원가입
    post<UserRoutes.Register> {
        val request = call.receive<UserRegisterRequest>()

        val user = service.register(request)
        val token = Token.createToken(application.environment.config, user.uuid.toString())
        call.respond(HttpStatusCode.OK, ResponseDto.Success(UserInfoWithToken(user, token), message = "성공"))
    }


    authenticate("refresh-jwt") {

        // 자동 로그인
        post<UserRoutes.Login.Auto> {
            val uuid = call.principal<UserIdPrincipal>()!!.name
            val user = service.autoLogin(UUID.fromString(uuid))
            call.respond(
                HttpStatusCode.OK,
                ResponseDto.Success(UserInfoWithToken(user, call.getToken()), message = "성공")
            )
        }

        // 리프레쉬 토큰 재발급
        post<UserRoutes.RefreshToken> {
            val token = call.getToken()
            call.respond(
                ResponseDto.Success(token, "재발급에 성공하였습니다.")
            )
        }

    }

}