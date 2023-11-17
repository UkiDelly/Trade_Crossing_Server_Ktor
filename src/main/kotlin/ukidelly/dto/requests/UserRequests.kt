package ukidelly.dto.requests

import kotlinx.serialization.Serializable
import ukidelly.systems.models.DefaultProfile
import ukidelly.systems.models.LoginType

@Serializable
data class SocialLoginRequestDto(val snsId: String, val email: String, val loginType: LoginType)

@Serializable
data class EmailLoginReqeustDto(val email: String, val password: String)


@Serializable
data class UserRegisterRequestDto(
    val snsId: String,
    val loginType: LoginType,
    val profile: DefaultProfile,
    val email: String,
    val password: String? = null,
    val userName: String,
    val islandName: String,
    val introduction: String
) {


    fun validEmail(): Boolean {
        val regex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
        return regex.matches(email)
    }
}