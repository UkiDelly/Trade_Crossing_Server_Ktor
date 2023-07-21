package ukidelly.domain.user

import kotlinx.serialization.Serializable
import java.time.Instant


@Serializable
data class UserRegisterRequest(
    val snsId: String,
    val loginMethod: LoginMethod,
    val email: String,
    val userName: String,
    val islandName: String,
    val introduction: String
) {

    fun toUser(): User = User(
        snsId = this.snsId,
        email = this.email,
        userName = this.userName,
        islandName = this.islandName,
        introduction = this.introduction,
        loginMethod = this.loginMethod,
        createdAt = Instant.now().toString()

    )
}