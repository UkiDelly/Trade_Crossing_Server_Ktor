package ukidelly.modules

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.*
import ukidelly.systems.errors.InvalidJwtTokenException
import ukidelly.systems.models.ResponseDto
import ukidelly.systems.models.Token
import ukidelly.utils.Utils
import java.time.LocalDateTime


fun Application.configureJWT() {

	// Please read the jwt property from the config file if you are using EngineMain
	val jwtAudience = environment.config.property("jwt.audience").getString()//"jwt-audience"
	val jwtIssuer = environment.config.property("jwt.issuer").getString() //"https://jwt-provider-domain/"
	val jwtRealm = environment.config.property("jwt.realm").getString()
	val jwtSecret = environment.config.property("jwt.secret").getString()

	install(Authentication) {
		jwt("auth-jwt") {
			realm = jwtRealm
			verifier(
				JWT
					.require(Algorithm.HMAC256(jwtSecret))
					.withAudience(jwtAudience)
					.withIssuer(jwtIssuer)
					.build()
			)
			validate { credential ->

				val now = LocalDateTime.now()
				val validToken = now.isBefore(Utils.convertDateToLocalDate(credential.expiresAt!!))

				if (!validToken || credential.payload.getClaim("type").asString() != "access_token") {
					throw InvalidJwtTokenException()

				}
				val userId = credential.payload.subject
				UserIdPrincipal(userId)
			}
		}

		jwt("refresh-jwt") {
			realm = jwtRealm
			verifier(
				JWT
					.require(Algorithm.HMAC256(jwtSecret))
					.withAudience(jwtAudience)
					.withIssuer(jwtIssuer)
					.build()
			)

			validate { credential ->
				val now = LocalDateTime.now()
				val validToken = now.isBefore(Utils.convertDateToLocalDate(credential.expiresAt!!))
				if (!validToken || credential.payload.getClaim("type").asString() != "refresh_token") {
					throw InvalidJwtTokenException()

				}
				val userId = credential.payload.subject
				val token = Token.createToken(application.environment.config, userId)
				this.attributes.put(tokenKey, token)
				UserIdPrincipal(userId)
			}

			challenge { defaultScheme, realm ->
				call.respond(
					HttpStatusCode.Unauthorized,
					ResponseDto.Error(error = "유효하지 않는 토큰입니다.", message = "실패")
				)
			}
		}
	}
}


private val tokenKey = AttributeKey<Token>("token")
fun ApplicationCall.getToken(): Token {
	return attributes[tokenKey]
}