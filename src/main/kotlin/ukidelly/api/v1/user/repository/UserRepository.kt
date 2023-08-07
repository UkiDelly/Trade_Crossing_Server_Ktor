package ukidelly.api.v1.user.repository

import ukidelly.api.v1.user.models.User
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.UserTable
import ukidelly.systems.models.LoginType


object UserRepository {


    suspend fun findUser(snsId: String, email: String, loginType: LoginType): User? {

        return dbQuery {

            UserEntity.find {
                UserTable.loginType eq loginType
                UserTable.snsId eq snsId
                UserTable.email eq email

            }.firstOrNull()?.toUser()
        }

    }


    suspend fun addNewUser(registerRequest: UserRegisterRequest): User? {

        val user = findUser(registerRequest.snsId, registerRequest.email, registerRequest.loginType)


        return if (user != null) {
            null
        } else {
            UserEntity.new {
                this.snsId = registerRequest.snsId
                this.email = registerRequest.email
                this.userName = registerRequest.userName
                this.islandName = registerRequest.islandName
                this.introduction = registerRequest.introduction
                this.loginType = registerRequest.loginType
            }.toUser()

        }


    }


}