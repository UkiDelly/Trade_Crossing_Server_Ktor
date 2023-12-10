package ukidelly.modules

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.slf4j.LoggerFactory
import ukidelly.dto.responses.ResponseDto
import ukidelly.systems.models.TokenType
import ukidelly.utils.Utils
import java.time.LocalDateTime
import java.util.*


//fun Application.configureJWT() {
//
//    // Please read the jwt property from the config file if you are using EngineMain
//    val jwtAudience = environment.config.property("jwt.audience").getString() //"jwt-audience"
//    val jwtIssuer = environment.config.property("jwt.issuer").getString() //"https://jwt-provider-domain/"
//    val jwtRealm = environment.config.property("jwt.realm").getString()
//    val jwtSecret = environment.config.property("jwt.secret").getString()
//
//    install(Authentication) {
//        jwt("auth-jwt") {
//            realm = jwtRealm
//            verifier(
//                JWT
//                    .require(Algorithm.HMAC256(jwtSecret))
//                    .withAudience(jwtAudience)
//                    .withIssuer(jwtIssuer)
//                    .build()
//            )
//            validate { credential ->
//
//                val now = LocalDateTime.now()
//                val validToken = now.isBefore(Utils.convertDateToLocalDate(credential.expiresAt!!))
//
//                if (!validToken || credential.payload.getClaim("type").asString() != "access_token") {
//                    throw InvalidJwtTokenException()
//
//                }
//                val userId = credential.payload.subject
//                UserIdPrincipal(userId)
//            }
//        }
//
//        jwt("refresh-jwt") {
//            realm = jwtRealm
//            verifier(
//                JWT
//                    .require(Algorithm.HMAC256(jwtSecret))
//                    .withAudience(jwtAudience)
//                    .withIssuer(jwtIssuer)
//                    .build()
//            )
//
//            validate { credential ->
//                val now = LocalDateTime.now()
//                val validToken = now.isBefore(Utils.convertDateToLocalDate(credential.expiresAt!!))
//                if (!validToken || credential.payload.getClaim("type").asString() != "refresh_token") {
//                    throw InvalidJwtTokenException()
//
//                }
//                val userId = credential.payload.subject
//                val token = Token.createToken(application.environment.config, userId)
//                this.attributes.put(tokenKey, token)
//                UserIdPrincipal(userId)
//            }
//
//            challenge { _, _ ->
//                call.respond(
//                    HttpStatusCode.Unauthorized,
//                    ResponseDto.Error(error = "유효하지 않는 토큰입니다.", message = "실패")
//                )
//            }
//        }
//    }
//}


//private val tokenKey = AttributeKey<Token>("token")
//fun ApplicationCall.getToken(): Token {
//    return attributes[tokenKey]
//}


fun Route.withAuth(tokenType: TokenType, route: Route.() -> Unit): Route {
    val child = createChild(AuthRouter()).apply {
        install(JwtAuthPlugin) { this.tokenType = tokenType }
        route()
    }
    return child
}


private val userIdKey = AttributeKey<UUID>("userId")
fun ApplicationCall.getUserId(): UUID = attributes[userIdKey]

class AuthRouter : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation =
        RouteSelectorEvaluation.Transparent

    override fun toString(): String {
        return "{ AuthRoute }"
    }
}

class AuthPluginConfiguration {
    lateinit var tokenType: TokenType
}

val JwtAuthPlugin = createRouteScopedPlugin(
    name = "JwtAuthPlugin",
    createConfiguration = ::AuthPluginConfiguration
) {
    pluginConfig.apply {
        val config = applicationConfig!!
        val logger = LoggerFactory.getLogger("JwtAuthPlugin")

        val jwtAudience = config.property("jwt.audience").getString() //"jwt-audience"
        val jwtIssuer = config.property("jwt.issuer").getString() //"https://jwt-provider-domain/"
        val jwtRealm = config.property("jwt.realm").getString()
        val jwtSecret = config.property("jwt.secret").getString()
        val verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .build()
        val now = LocalDateTime.now()

        onCall { call ->
            if (call.request.authorization() == null) {
                call.respond(HttpStatusCode.Unauthorized, ResponseDto.Error(error = "인증되지 않은 요청입니다.", message = "실패"))
                return@onCall
            }

            val token = call.request.authorization()!!.split(" ").last()
            verifier.verify(token).run {
                val expiration = this.expiresAt
                TokenType.valueOf(this.getClaim("type").asString()).run {
                    if (this != tokenType) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ResponseDto.Error(error = "유효하지 않는 토큰입니다.", message = "실패")
                        )
                        return@onCall
                    }
                }

                if (now.isAfter(Utils.convertDateToLocalDate(expiration))) throw TokenExpiredException(
                    "유효하지 않은 토큰입니다.",
                    expiration.toInstant()
                )

                val userId = this.subject
                call.attributes.put(userIdKey, UUID.fromString(userId))
            }
        }
    }

}
