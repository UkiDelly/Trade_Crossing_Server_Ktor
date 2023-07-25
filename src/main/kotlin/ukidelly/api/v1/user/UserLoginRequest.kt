package ukidelly.api.v1.user

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginRequest(val snsId: String, val email: String)