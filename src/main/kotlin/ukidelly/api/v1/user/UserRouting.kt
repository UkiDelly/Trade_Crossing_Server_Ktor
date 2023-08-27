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
import ukidelly.api.v1.user.models.EmailLoginReqeust
import ukidelly.api.v1.user.models.SocialLoginRequest
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.api.v1.user.models.UserResponse
import ukidelly.api.v1.user.service.UserService
import ukidelly.modules.getToken
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

			logger.debug("body: {}", request)
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
					val token = Token.createToken(application.environment.config, user.userId.toString())
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

		authenticate("refresh-jwt") {

			post("/refresh") {
				val token = call.getToken()
				call.respond(HttpStatusCode.OK, ResponseDto.Success(data = token, "재발급에 성공하였습니다."))
			}

			post("/auto") {
				val userId = call.principal<UserIdPrincipal>()!!.name
				val user = withContext(Dispatchers.IO) {
					service.autoLogin(UUID.fromString(userId))
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
		service.register(request)?.let {
			val token = Token.createToken(application.environment.config, it.userId.toString())
			call.respond(HttpStatusCode.OK, ResponseDto.Success(UserResponse(it, token), message = "회원가입 성공"))
		} ?: run {
			call.respond(HttpStatusCode.Conflict, ResponseDto.Error("이미 가입한 유저입니다.", message = "회원가입 실패"))
		}

	}

}