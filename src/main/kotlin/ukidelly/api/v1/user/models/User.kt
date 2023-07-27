package ukidelly.api.v1.user.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.database.models.user.LoginType
import java.util.*

// Entity
// UUIDEntity를 상속받는 User 클래스를 정의합니다.

@Serializable
data class User(

    @Contextual
    val userId: UUID,
    val snsId: String,
    val email: String,
    val userName: String,
    val islandName: String,
    val introduction: String,
    val loginType: LoginType
)
