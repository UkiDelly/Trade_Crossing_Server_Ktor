package ukidelly.database.models.user

import ukidelly.api.v1.user.models.User
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.database.DataBaseFactory.dbQuery


object UserRepository {


    suspend fun getUserBySnsId(snsId: String): User? = dbQuery {

        UserEntity.find {
            UserTable.snsId eq snsId
        }.firstOrNull()?.let {
            it.toUser()
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
            }.toUser()
        }
    }


}