package ukidelly.domain.system

import kotlinx.serialization.Serializable

@Serializable
data class Token(val accessToken: String, val refreshToken: String)