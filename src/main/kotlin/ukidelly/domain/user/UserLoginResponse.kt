package ukidelly.domain.user

import kotlinx.serialization.Serializable
import ukidelly.domain.system.Token

@Serializable
class UserLoginResponse(val user: User, val token: Token)