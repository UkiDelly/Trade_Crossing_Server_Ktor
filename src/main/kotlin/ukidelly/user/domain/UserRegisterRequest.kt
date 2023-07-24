package ukidelly.user.domain


data class UserRegisterRequest(
    val snsId: String,
    val loginType: LoginType,
    val email: String,
    val userName: String,
    val islandName: String,
    val introduction: String
) {

    fun toUserEntity(): UserEntity = UserEntity {
        this.snsId = this@UserRegisterRequest.snsId
        this.loginType = this@UserRegisterRequest.loginType
        this.email = this@UserRegisterRequest.email
        this.userName = this@UserRegisterRequest.userName
        this.islandName = this@UserRegisterRequest.islandName
        this.introduction = this@UserRegisterRequest.introduction

    }
}