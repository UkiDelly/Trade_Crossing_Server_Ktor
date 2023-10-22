package ukidelly.api.v1.user.models

import kotlinx.serialization.Serializable
import ukidelly.systems.models.Token

@Serializable
data class UserInfoWithToken(val user: User, val token: Token)