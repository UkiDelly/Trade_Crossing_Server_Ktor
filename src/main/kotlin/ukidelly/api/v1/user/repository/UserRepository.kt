package ukidelly.api.v1.user.repository


import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Module
import org.slf4j.LoggerFactory
import ukidelly.api.v1.user.UserRegisterRequest
import ukidelly.database.models.UserEntity
import ukidelly.database.models.UserTable


@Module
class UserRepository {

    val logger = LoggerFactory.getLogger("UserRepository")
    fun findUser(snsId: String, email: String): UserEntity? {

        return transaction {
            addLogger(StdOutSqlLogger)

            // Users 테이블에서 userId로 조회
            UserEntity.find {
                (UserTable.snsId eq snsId) and (UserTable.email eq email)
            }.firstOrNull()


        }

    }


    fun addUser(userRegisterRequest: UserRegisterRequest): UserEntity {
        return transaction {
            addLogger(StdOutSqlLogger)


            // Users 테이블에 새로운 유저 추가
            val user = UserEntity.new {
                snsId = userRegisterRequest.snsId
                email = userRegisterRequest.email
                loginType = userRegisterRequest.loginType
                userName = userRegisterRequest.userName
                islandName = userRegisterRequest.islandName
                introduction = userRegisterRequest.introduction
            }
            logger.debug("user: {}", user)
            user

        }
    }

}