package ukidelly.api.v1.user

import ukidelly.database.models.LoginType
import java.util.*


data class UserResponse(
    val userId: UUID,
    val snsId: String,
    val email: String,
    val userName: String,
    val islandName: String,
    val introduction: String,
    val loginType: LoginType
)