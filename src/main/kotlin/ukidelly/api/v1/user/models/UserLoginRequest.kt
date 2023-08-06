package ukidelly.api.v1.user.models

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginRequest(val snsId: String, val email: String)