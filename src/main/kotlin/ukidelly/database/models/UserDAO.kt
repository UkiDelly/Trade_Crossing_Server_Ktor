package ukidelly.database.models

import ukidelly.api.v1.user.UserRegisterRequest
import ukidelly.database.DataBaseFactory.dbQuery

interface UserDAO {
    suspend fun getUserBySnsId(snsId: String): User?
    suspend fun addNewUser(registerRequest: UserRegisterRequest): User?

}

class UserDAOImpl : UserDAO {

    private fun resultRowToUser(userEntity: UserEntity) = User(
        userId = userEntity.id.value,
        snsId = userEntity.snsId,
        email = userEntity.email,
        userName = userEntity.userName,
        islandName = userEntity.islandName,
        introduction = userEntity.introduction,
        loginType = userEntity.loginType
    )

    override suspend fun getUserBySnsId(snsId: String): User? = dbQuery {

        // Users 테이블에서 snsId로 조회
        UserEntity.find {
            UserTable.snsId eq snsId
        }.firstOrNull()?.let {
            resultRowToUser(it)
        }
    }


    override suspend fun addNewUser(registerRequest: UserRegisterRequest): User? {
        TODO("Not yet implemented")
    }


}