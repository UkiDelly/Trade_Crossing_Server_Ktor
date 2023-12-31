package ukidelly.systems.models

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId


@Serializable
data class Token(val accessToken: String, val refreshToken: String)

fun ApplicationConfig.generateToken(userId: String): Token {
    val audience = this.property("jwt.audience").getString()
    val issuer = this.property("jwt.issuer").getString()
    val secret = this.property("jwt.secret").getString()

    val accessToken = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withSubject(userId)
        .withClaim("type", TokenType.access.name)
        .withExpiresAt(
            Instant.now().plusSeconds(60 * 60 * 24).atZone(ZoneId.systemDefault()).toInstant()
        )
        .sign(Algorithm.HMAC256(secret))

    val refreshToken = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withSubject(userId)
        .withClaim("type", TokenType.refresh.name)
        .withExpiresAt(Instant.now().plusSeconds(60 * 60 * 24 * 30).atZone(ZoneId.systemDefault()).toInstant())
        .sign(Algorithm.HMAC256(secret))

    return Token(accessToken = accessToken, refreshToken = refreshToken)
}
