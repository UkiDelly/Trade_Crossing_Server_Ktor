package ukidelly.api.v1.user.models

import kotlinx.serialization.Serializable
import ukidelly.database.models.user.User
import ukidelly.systems.models.Token

@Serializable
class UserResponse(val user: User, val token: Token)