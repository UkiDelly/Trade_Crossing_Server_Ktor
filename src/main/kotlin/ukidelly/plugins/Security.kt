package ukidelly.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ukidelly.utils.DateUtil
import java.time.LocalDateTime


fun Application.configureSecurity() {

    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = environment.config.property("jwt.audience").getString()//"jwt-audience"
    val jwtDomain = environment.config.property("jwt.domain").getString() //"https://jwt-provider-domain/"
    val jwtRealm = environment.config.property("jwt.realm").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->

                val date = LocalDateTime.now()
                val validToken = date.isBefore(DateUtil.convertDateToLocalDate(credential.expiresAt!!))

                if (!validToken) {
                    return@validate null

                }
                val userId = credential.payload.getClaim("userId").asString()
                UserIdPrincipal(userId)
            }
        }

        jwt("refresh-jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )

            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }

}
