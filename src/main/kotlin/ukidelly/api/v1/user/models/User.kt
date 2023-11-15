package ukidelly.api.v1.user.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ukidelly.database.models.user.UserEntity
import ukidelly.systems.models.DefaultProfile
import ukidelly.systems.models.LoginType
import java.util.*

// Entity
// UUIDEntity를 상속받는 User 클래스를 정의합니다.

@Serializable
data class User(
    @Contextual
    val uuid: UUID,
    val userId: Int,
    val snsId: String,
    val email: String,
    val password: String?,
    val profile: DefaultProfile,
    val userName: String,
    val islandName: String,
    val introduction: String,
    val loginType: LoginType
) {
    constructor(userEntity: UserEntity) : this(
        userId = userEntity.id.value,
        uuid = userEntity.uuid,
        snsId = userEntity.snsId,
        email = userEntity.email,
        password = userEntity.password,
        profile = userEntity.defaultProfile,
        userName = userEntity.userName,
        islandName = userEntity.islandName,
        introduction = userEntity.introduction,
        loginType = userEntity.loginType

    )
}


@Serializable
data class CompactUser(
    val userId: Int,
    @Contextual
    val uuid: UUID,
    val userName: String,
    val islandName: String
) {

    constructor(entity: UserEntity) : this(
        userId = entity.id.value,
        uuid = entity.uuid,
        userName = entity.userName,
        islandName = entity.islandName
    )
}
