package ukidelly.database.models.user

import org.koin.core.annotation.Module
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.database.DataBaseFactory.dbQuery


@Module
class UserDao {

    private fun resultRowToUser(userEntity: UserEntity) = User(
        userId = userEntity.id.value,
        snsId = userEntity.snsId,
        email = userEntity.email,
        userName = userEntity.userName,
        islandName = userEntity.islandName,
        introduction = userEntity.introduction,
        loginType = userEntity.loginType
    )

    suspend fun getUserBySnsId(snsId: String): User? = dbQuery {

        UserEntity.find {
            UserTable.snsId eq snsId
        }.firstOrNull()?.let {
            resultRowToUser(it)
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
                resultRowToUser(it)
            }
        }
    }


}