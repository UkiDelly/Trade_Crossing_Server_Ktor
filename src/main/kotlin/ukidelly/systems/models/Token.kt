package ukidelly.systems.models

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId


@Serializable
class Token(val accessToken: String, val refreshToken: String) {


    companion object {
        fun createToken(config: ApplicationConfig, userId: String): Token {
            val audience = config.property("jwt.audience").getString()
            val issuer = config.property("jwt.issuer").getString()
            val secret = config.property("jwt.secret").getString()

            val accessToken = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withSubject(userId)
                .withClaim("type", "access_token")
                .withExpiresAt(
                    Instant.now().plusSeconds(60 * 60 * 24).atZone(ZoneId.systemDefault()).toInstant()
                )
                .sign(Algorithm.HMAC256(secret))

            val refreshToken = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withSubject(userId)
                .withClaim("type", "refresh_token")
                .withExpiresAt(Instant.now().plusSeconds(60 * 60 * 24 * 30).atZone(ZoneId.systemDefault()).toInstant())
                .sign(Algorithm.HMAC256(secret))


            return Token(accessToken, refreshToken)
        }
    }
}


