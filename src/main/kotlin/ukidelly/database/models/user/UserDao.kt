package ukidelly.database.models.user

import ukidelly.api.v1.user.models.User
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.database.DataBaseFactory.dbQuery


object UserDao {

    private fun resultRowToModel(entity: UserEntity): User = User(
        userId = entity.id.value,
        snsId = entity.snsId,
        email = entity.email,
        userName = entity.userName,
        islandName = entity.islandName,
        introduction = entity.introduction,
        loginType = entity.loginType
    )

    suspend fun getUserBySnsId(snsId: String): User? = dbQuery {

        UserEntity.find {
            UserTable.snsId eq snsId
        }.firstOrNull()?.let {
            resultRowToModel(it)
        }
    }


    suspend fun addNewUser(registerRequest: UserRegisterRequest): User? {
        return dbQuery {
            getUserBySnsId(registerRequest.snsId)?.let {
                return@dbQuery null
            }

            UserEntity.new {
                snsId = registerRequest.snsId
                email = registerRequest.email
                userName = registerRequest.userName
                islandName = registerRequest.islandName
                introduction = registerRequest.introduction
                loginType = registerRequest.loginType
            }.let {
                resultRowToModel(it)
            }
        }
    }


}