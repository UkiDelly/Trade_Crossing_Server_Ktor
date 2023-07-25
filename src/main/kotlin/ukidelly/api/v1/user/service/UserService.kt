package ukidelly.api.v1.user.service

import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Module
import ukidelly.api.v1.user.UserLoginRequest
import ukidelly.api.v1.user.UserRegisterRequest
import ukidelly.api.v1.user.repository.UserRepository
import ukidelly.database.models.User
import ukidelly.database.models.UserDAOImpl
import ukidelly.database.models.UserEntity
import java.util.*

@Module
class UserService(
    private val userRepository: UserRepository
) {


    /**
     * 로그인
     * @param loginRequest 로그인 요청 Dto
     * @return [UserEntity?] 유저 정보
     */
    suspend fun login(loginRequest: UserLoginRequest): User? {
        val dao = UserDAOImpl()

        return dao.getUserBySnsId(loginRequest.snsId)
    }


    /**
     * 회원가입
     * @param userRegisterRequest 회원가입 요청 Dto
     * @return [UserEntity]? 유저 정보로, 유저가 존재하면 [null]을 반환, 존재하지 않아 가입에 성공하면 [UserEntity]를 반환

     */
    fun register(userRegisterRequest: UserRegisterRequest): UserEntity =
        userRepository.addUser(userRegisterRequest)


}
