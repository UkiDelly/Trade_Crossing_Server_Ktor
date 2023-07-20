package ukidelly.domain.user

class UserRegisterRequest(
    snsId: String,
    loginMethod: LoginMethod,
    email: String,
    userName: String,
    islandName: String,
    introduction: String
)