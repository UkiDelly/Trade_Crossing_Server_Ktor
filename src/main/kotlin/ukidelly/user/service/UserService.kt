package ukidelly.user.service

import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject
import org.ktorm.database.Database
import org.ktorm.dsl.*
import ukidelly.user.domain.UserEntity
import ukidelly.user.domain.UserLoginRequest
import ukidelly.user.domain.UserRegisterRequest
import ukidelly.user.domain.UserTable
import java.util.*

@Module
class UserService {


    private val database by inject<Database>(Database::class.java)

    /**
     * 로그인
     * @param loginRequest 로그인 요청 Dto
     * @return [UserEntity?] 유저 정보
     */
    fun login(loginRequest: UserLoginRequest): UserEntity? {
        // 데이터 베이스에서 유저 정보를 조회
        return database.from(UserTable).select()
            .where {
                (UserTable.snsId eq loginRequest.snsId) and (UserTable.email eq loginRequest.email)
            }.map { row ->

                // 조회된 유저 정보를 UserEntity로 변환
                UserTable.createEntity(row)

            }.firstOrNull()
    }


    /**
     * 회원가입
     * @param userRegisterRequest 회원가입 요청 Dto
     * @return [UserEntity]? 유저 정보로, 유저가 존재하면 [null]을 반환, 존재하지 않아 가입에 성공하면 [UserEntity]를 반환

     */
    fun register(userRegisterRequest: UserRegisterRequest): UserEntity? {
        val userExist = database.from(UserTable).select()
            .where {
                (UserTable.snsId eq userRegisterRequest.snsId) and (UserTable.email eq userRegisterRequest.email)
            }.map { row ->
                UserTable.createEntity(row)
            }.firstOrNull().let { it != null }

        if (userExist) {
            return null
        }

        return database.insertAndGenerateKey(UserTable) {
            set(it.snsId, userRegisterRequest.snsId)
            set(it.email, userRegisterRequest.email)
            set(it.loginType, userRegisterRequest.loginType)
            set(it.userName, userRegisterRequest.userName)
            set(it.islandName, userRegisterRequest.islandName)
            set(it.introduction, userRegisterRequest.introduction)
        }.let { key ->
            database.from(UserTable).select()
                .where { key as UUID eq UserTable.userId }
                .map { row ->
                    UserTable.createEntity(row)
                }.first()
        }
    }


}
