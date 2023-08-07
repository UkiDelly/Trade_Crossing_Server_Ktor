package ukidelly.api.v1.user.models

import kotlinx.serialization.Serializable
import ukidelly.systems.models.LoginType

@Serializable
data class UserRegisterRequest(
    val snsId: String,
    val loginType: LoginType,
    val email: String,
    val userName: String,
    val islandName: String,
    val introduction: String
) {


    fun validEmail(): Boolean {

        val regex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
        return regex.matches(email)
    }
}