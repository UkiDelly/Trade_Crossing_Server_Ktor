package ukidelly.api.v2.user


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.dto.requests.EmailLoginReqeustDto
import ukidelly.dto.requests.SocialLoginRequestDto
import ukidelly.dto.requests.UserRegisterRequestDto
import ukidelly.dto.responses.ResponseDto
import ukidelly.dto.responses.UserWithTokenDto
import ukidelly.modules.getUserId
import ukidelly.modules.withAuth
import ukidelly.services.UserService
import ukidelly.systems.models.Token
import ukidelly.systems.models.TokenType
import ukidelly.systems.models.generateToken
import java.util.*

fun Route.userRouting() {

    fun getToken(userId: String): Token = environment!!.config.generateToken(userId)


    val logger = LoggerFactory.getLogger("UserRouting")
    val service by inject<UserService>()


    // 이메일로 로그인
    post<UserRoutes.Login.EmailLogin> {
        val request = call.receive<EmailLoginReqeustDto>()

        val user = service.emailLogin(request.email, request.password)
        val token = Token.createToken(application.environment.config, user.uuid.toString())
        call.respond(HttpStatusCode.OK, ResponseDto.Success(UserWithTokenDto(user, token), "성공"))
    }

    // 소셜 로그인
    post<UserRoutes.Login.SocialLogin> {
        val request = call.receive<SocialLoginRequestDto>()
        val user = service.socialLogin(request.snsId, email = request.email, loginType = request.loginType)
        val token = Token.createToken(application.environment.config, user.uuid.toString())
        call.respond(
            HttpStatusCode.OK,
            ResponseDto.Success(UserWithTokenDto(user = user, token = token), message = "성공")
        )
    }

    // 회원가입
    post<UserRoutes.Register> {
        val request = call.receive<UserRegisterRequestDto>()

        val user = service.register(request)
        val token = Token.createToken(application.environment.config, user.uuid.toString())
        call.respond(HttpStatusCode.OK, ResponseDto.Success(UserWithTokenDto(user, token), message = "성공"))
    }


    withAuth(TokenType.refresh) {

        // 자동 로그인
        post<UserRoutes.Login.Auto> {
            val uuid = call.principal<UserIdPrincipal>()!!.name
            val user = service.autoLogin(UUID.fromString(uuid))
            call.respond(
                HttpStatusCode.OK,
                ResponseDto.Success(
                    UserWithTokenDto(user, getToken(user.userId.toString())),
                    message = "성공"
                )
            )
        }

        // 리프레쉬 토큰 재발급
        post<UserRoutes.RefreshToken> {
            val userId = call.getUserId()
            val token = getToken(userId.toString())
            call.respond(
                ResponseDto.Success(token, "재발급에 성공하였습니다.")
            )
        }

    }

}