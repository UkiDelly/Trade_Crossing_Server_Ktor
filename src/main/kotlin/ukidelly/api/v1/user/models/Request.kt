package ukidelly.api.v1.user.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ukidelly.systems.models.DefaultProfile
import ukidelly.systems.models.LoginType

@Serializable
data class SocialLoginRequest(val snsId: String, val email: String) {
    fun toJsonString(): String {
        return """{"sns_id": "$snsId", "email": "$email"}"""
    }
}

@Serializable
data class EmailLoginReqeust(val email: String, val password: String) {
    fun toJsonString(): String {
        return Json.encodeToString(this)
    }
}


@Serializable
data class UserRegisterRequest(
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