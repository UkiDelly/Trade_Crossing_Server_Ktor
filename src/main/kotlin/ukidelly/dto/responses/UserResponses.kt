package ukidelly.dto.responses

import kotlinx.serialization.Serializable
import ukidelly.models.User
import ukidelly.systems.models.Token

@Serializable
data class UserWithTokenDto(val user: User, val token: Token)