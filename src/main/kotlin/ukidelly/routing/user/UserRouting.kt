package ukidelly.routing.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.MongoClientSettings
import com.mongodb.MongoException
import com.mongodb.client.MongoClients
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import ukidelly.domain.system.ResponseModel
import ukidelly.domain.system.Token
import ukidelly.domain.user.UserLoginRequest
import ukidelly.domain.user.UserLoginResponse
import ukidelly.domain.user.UserRegisterRequest
import ukidelly.repository.UserRepository
import ukidelly.service.UserService
import java.time.Instant


@OptIn(InternalAPI::class)
fun Route.userRouting(mongoClientSetting: MongoClientSettings) {

    val logger = LoggerFactory.getLogger("UserRouting")


    val jwtAudience = environment!!.config.property("jwt.audience").getString()//"jwt-audience"
    val jwtDomain = environment!!.config.property("jwt.domain").getString() //"https://jwt-provider-domain/"
    val jwtSecret = environment!!.config.property("jwt.secret").getString()

    val database = environment!!.config.property("mongodb.database").getString()
    val mongoClient = MongoClients.create(mongoClientSetting)
    val userCollection =
        mongoClient.getDatabase(database).getCollection("user")


    val service by inject<UserService>()
    service.apply {
        val repository by inject<UserRepository>()
        setRepository(repository, userCollection)
    }

    //
    get("login") {

        // 요청의 body를 가져와서 UserLoginRequestModel로 변환
        val request = call.receive<UserLoginRequest>()

        logger.debug("request: {}", request)

        val user = withContext(Dispatchers.IO) {
            service.findOne(request)
        }.also { mongoClient.close() } ?: run {
            mongoClient.close()
            call.respond(status = HttpStatusCode.NotFound, "존재하지 않는 유저입니다.")
            return@get
        }


        // 토큰 생성
        val token = createJwtToken(
            jwtAudience = jwtAudience,
            jwtDomain = jwtDomain,
            jwtSecret = jwtSecret,
            userId = user.id.toString()
        ) // Token(accessToken, refreshToken)

        // 응답
        call.respond(
            HttpStatusCode.OK, ResponseModel(
                UserLoginResponse(
                    user,
                    token
                ), "로그인 성공"
            )
        )

    }



    post("register") {


        // 요청의 body를 가져와서 UserRegisterRequestModel로 변환
        val request = call.receive<UserRegisterRequest>()


        // User으로 변환
        val user = request.toUser()

        // DB에 저장 및 에러 처리
        try {
            withContext(Dispatchers.IO) {
                service.register(user)
            }
        } catch (e: Throwable) {
            when (e) {
                is MongoException -> {
                    call.respond(status = HttpStatusCode.InternalServerError, "DB 에러")
                    return@post
                }

                is IllegalStateException -> {
                    call.respond(status = HttpStatusCode.Conflict, "이미 존재하는 유저입니다.")
                    return@post
                }

                else -> {
                    logger.error(e.message)
                    call.respond(status = HttpStatusCode.ExpectationFailed, "회원가입 실패")
                    return@post
                }
            }
        } finally {
            mongoClient.close()
        }

        // 토큰 모델 생성
        val token = createJwtToken(
            jwtAudience = jwtAudience,
            jwtDomain = jwtDomain,
            jwtSecret = jwtSecret,
            userId = user.id.toString()
        )

        // 로그인 시키기
        val response = UserLoginResponse(user, token)

        call.respond(HttpStatusCode.OK, ResponseModel(response, "회원가입 성공"))

    }

    //
    authenticate("refresh-jwt") {
        //
        post("refresh") { }

    }
}


private fun createJwtToken(

    jwtDomain: String,
    jwtAudience: String,
    jwtSecret: String,
    userId: String
): Token {


    // 현재 시간 기록
    val now = Instant.now()

    // 5분 유호 기간을 가진 accessToken 생성
    var expiredAt = now.plusSeconds(60 * 5)
    val accessToken = JWT.create()
        .withIssuer(jwtDomain)
        .withAudience(jwtAudience)
        .withClaim("userId", userId)
        .withExpiresAt(expiredAt)
        .sign(Algorithm.HMAC256(jwtSecret))

    // 30일 유효 기간을 가진 refreshToken 생성
    expiredAt = now.plusSeconds(60 * 60 * 24 * 30)
    val refreshToken = JWT.create()
        .withIssuer(jwtDomain)
        .withAudience(jwtAudience)
        .withExpiresAt(expiredAt)
        .sign(Algorithm.HMAC256(jwtSecret))

    // 토큰 모델 생성
    return Token(accessToken, refreshToken)

}
