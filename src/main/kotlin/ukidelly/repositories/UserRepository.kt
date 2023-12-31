package ukidelly.repositories

import org.koin.core.annotation.Single
import ukidelly.database.DataBaseFactory.dbQuery
import ukidelly.database.models.user.UserEntity
import ukidelly.database.models.user.Users
import ukidelly.dto.requests.UserRegisterRequestDto
import ukidelly.models.User
import ukidelly.systems.models.LoginType
import java.util.*


@Single
class UserRepository {

    /**
     * 이메일 유저 찾기
     * @param email 이메일
     * @param password 비밀번호
     * @return [User]? 유저정보
     */
    suspend fun findEmailUser(email: String): UserEntity? {
        return dbQuery {
            UserEntity.find {
                Users.loginType eq LoginType.email
                Users.email eq email
            }.firstOrNull()
        }
    }


    /**
     * 소셜 로그인
     * @param snsId 소셜 ID
     * @param email 이메일
     * @param loginType 소셜로그인 타입
     * @return [User]? 유저정보
     */
    suspend fun findSocialUser(snsId: String, email: String, loginType: LoginType): UserEntity? {
        return dbQuery {
            UserEntity.find {
                Users.loginType eq loginType
                Users.snsId eq snsId
                Users.email eq email
            }.firstOrNull()
        }
    }


    /**
     * UUID로 유저 찾기
     * @param userId 유저 아이디
     * @return [UserEntity]? 유저 Entity
     */
    suspend fun findUserByUUID(uuid: UUID): UserEntity? {
        return dbQuery {
            UserEntity.find { Users.uuid eq uuid }.firstOrNull()
        }
    }


    /**
     * 이메일로 유저 가입 여부 확인
     * @param email 이메일
     * @return [User]? 유저정보
     */
    suspend fun findUserByEmail(email: String): UserEntity? {
        return dbQuery {
            UserEntity.find {
                Users.email eq email
            }.firstOrNull()
        }
    }


    /**
     * 유저 회원가입
     * @param registerRequest 회원가입 요청 Dto
     * @return [User] 유저정보
     */
    suspend fun addNewUser(registerRequest: UserRegisterRequestDto, password: String? = null): UserEntity = dbQuery {
        UserEntity.new {
            this.snsId = registerRequest.snsId
            this.email = registerRequest.email
            this.password = password
            this.userName = registerRequest.userName
            this.islandName = registerRequest.islandName
            this.introduction = registerRequest.introduction
            this.loginType = registerRequest.loginType
            this.defaultProfile = registerRequest.profile
        }
    }
}