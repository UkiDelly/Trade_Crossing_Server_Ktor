package ukidelly.api.v2.user


import io.github.smiley4.ktorswaggerui.dsl.resources.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
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
import ukidelly.systems.errors.ServerError
import ukidelly.systems.models.Token
import ukidelly.systems.models.TokenType
import ukidelly.systems.models.generateToken
import java.util.*

fun Route.userRouting() {

  fun getToken(userId: String): Token = environment!!.config.generateToken(userId)

  val logger = LoggerFactory.getLogger("UserRouting")
  val service by inject<UserService>()


  // 이메일로 로그인
  post<UserRoutes.Login.EmailLogin>({
    description = "이메일로 로그인"
    tags = listOf("계정")
    request {
      body<EmailLoginReqeustDto>()
    }

    response {
      HttpStatusCode.OK to {
        description = "성공"
        body<UserWithTokenDto>()
      }

      HttpStatusCode.NotFound to {
        description = "존재하지 않는 계정입니다."
        body<ResponseDto.Error> {
          example("존재하지 않는 계정", ResponseDto.Error(ServerError.NotExist, "존재하지 않는 계정입니다."))
        }
      }

      HttpStatusCode.Forbidden to {
        description = "비밀번호가 틀렸습니다."
        body<ResponseDto.Error> {
          example("비밀번호가 틀렸습니다.", ResponseDto.Error(ServerError.PasswordIncorrect, "비밀번호가 틀렸습니다."))
        }
      }
    }
  }) {
    val request = call.receive<EmailLoginReqeustDto>()

    val user = service.emailLogin(request.email, request.password)
    val token = getToken(user.uuid.toString())
    call.respond(HttpStatusCode.OK, ResponseDto.Success(UserWithTokenDto(user, token), "성공"))
  }

  // 소셜 로그인
  post<UserRoutes.Login.SocialLogin>({
    description = "소셜 로그인"
    tags = listOf("계정")
    request {
      body<SocialLoginRequestDto>()
    }

    response {
      HttpStatusCode.OK to {
        description = "성공"
        body<UserWithTokenDto>()
      }

      HttpStatusCode.NotFound to {
        description = "존재하지 않는 계정입니다."
        body<ResponseDto.Error> {
          example("존재하지 않는 계정", ResponseDto.Error(ServerError.NotExist, "존재하지 않는 계정입니다."))
        }
      }
    }

  }) {
    val request = call.receive<SocialLoginRequestDto>()
    val user = service.socialLogin(request.snsId, email = request.email, loginType = request.loginType)
    val token = getToken(user.uuid.toString())
    call.respond(
      HttpStatusCode.OK,
      ResponseDto.Success(UserWithTokenDto(user = user, token = token), message = "성공")
    )
  }

  // 회원가입
  post<UserRoutes.Register>({
    description = "회원가입"
    tags = listOf("계정")
    request {
      body<UserRegisterRequestDto>()
    }

    response {
      HttpStatusCode.Created to {
        description = "성공"
        body<UserWithTokenDto>()
      }

      HttpStatusCode.Conflict to {
        description = "이미 존재하는 계정입니다."
        body<ResponseDto.Error> {
          example("이미 존재하는 계정입니다.", ResponseDto.Error(ServerError.UserExist, "이미 존재하는 계정입니다."))
        }
      }
    }

  }) {
    val request = call.receive<UserRegisterRequestDto>()

    val user = service.register(request)
    val token = getToken(user.uuid.toString())
    call.respond(HttpStatusCode.Created, ResponseDto.Success(UserWithTokenDto(user, token), message = "성공"))
  }


  withAuth(TokenType.refresh) {

    // 자동 로그인
    post<UserRoutes.Login.Auto>({
      securitySchemeName = "Refresh"
      protected = true
      tags = listOf("계정")
      request {
        headerParameter<String>("token") {
          description = "Bearer Token"
          required = true
        }
      }

      response {
        HttpStatusCode.OK to {
          description = "성공"
          body<UserWithTokenDto>()
        }

        HttpStatusCode.BadRequest to {
          description = "토큰이 유효하지 않습니다."
          body<ResponseDto.Error> {
            example("토큰이 유효하지 않습니다.", ResponseDto.Error(ServerError.Unauthorized, "토큰이 유효하지 않습니다."))
          }
        }

      }
    }) {
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
    post<UserRoutes.RefreshToken>({
      securitySchemeName = "Refresh"
      protected = true
      tags = listOf("계정")
      request {
        headerParameter<String>("token") {
          description = "Bearer Token"
          required = true
        }
      }

      response {
        HttpStatusCode.OK to {
          description = "성공"
          body<UserWithTokenDto>()
        }

        HttpStatusCode.BadRequest to {
          description = "토큰이 유효하지 않습니다."
          body<ResponseDto.Error> {
            example("토큰이 유효하지 않습니다.", ResponseDto.Error(ServerError.Unauthorized, "토큰이 유효하지 않습니다."))
          }
        }
      }

    }) {
      val userId = call.getUserId()
      val token = getToken(userId.toString())
      call.respond(
        ResponseDto.Success(token, "재발급에 성공하였습니다.")
      )
    }
  }
}