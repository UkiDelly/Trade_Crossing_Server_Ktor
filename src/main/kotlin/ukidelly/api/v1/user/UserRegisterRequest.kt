package ukidelly.api.v1.user

import ukidelly.database.models.LoginType


data class UserRegisterRequest(
    val snsId: String,
    val loginType: LoginType,
    val email: String,
    val userName: String,
    val islandName: String,
    val introduction: String
)