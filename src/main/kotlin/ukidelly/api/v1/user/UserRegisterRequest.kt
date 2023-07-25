package ukidelly.api.v1.user

import kotlinx.serialization.Serializable
import ukidelly.database.models.LoginType

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
        // TODO: 이메일 정규식 검사
        var regex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
        return regex.matches(email)
    }
}