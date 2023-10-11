package ukidelly.api.v1.user.repository

import org.koin.core.annotation.Module
import ukidelly.api.v1.user.models.User
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.UserTable
import ukidelly.systems.models.LoginType
import java.util.*


@Module
class UserRepository {

    /**
     * 이메일 유저 찾기
     * @param email 이메일
     * @param password 비밀번호
     * @return [User]? 유저정보
     */
    suspend fun findEmailUser(email: String): User? {
        return dbQuery {
            UserEntity.find {
                UserTable.loginType eq LoginType.email
                UserTable.email eq email
            }.firstOrNull()?.toUser()
        }
    }


    /**
     * 소셜 로그인
     * @param snsId 소셜 ID
     * @param email 이메일
     * @param loginType 소셜로그인 타입
     * @return [User]? 유저정보
     */
    suspend fun findSocialUser(snsId: String, email: String, loginType: LoginType): User? {
        return dbQuery {
            UserEntity.find {
                UserTable.loginType eq loginType
                UserTable.snsId eq snsId
                UserTable.email eq email
            }.firstOrNull()?.toUser()
        }
    }


    /**
     * UUID로 유저 찾기
     * @param userId 유저 아이디
     * @return [User]? 유저정보
     */
    suspend fun findUserById(userId: Int): User? {
        return dbQuery {
            UserEntity.findById(userId)?.toUser()
        }
    }


    /**
     * 이메일로 유저 가입 여부 확인
     * @param email 이메일
     * @return [User]? 유저정보
     */
    suspend fun findUserByEmail(email: String): User? {
        return dbQuery {
            UserEntity.find {
                UserTable.email eq email
            }.firstOrNull()?.toUser()
        }
    }


    /**
     * 유저 회원가입
     * @param registerRequest 회원가입 요청 Dto
     * @return [User] 유저정보
     */
    suspend fun addNewUser(registerRequest: UserRegisterRequest, password: String? = null): User = dbQuery {
        UserEntity.new {
            this.snsId = registerRequest.snsId
            this.email = registerRequest.email
            this.password = password
            this.userName = registerRequest.userName
            this.islandName = registerRequest.islandName
            this.introduction = registerRequest.introduction
            this.loginType = registerRequest.loginType
            this.defaultProfile = registerRequest.profile
        }.toUser()
    }
}