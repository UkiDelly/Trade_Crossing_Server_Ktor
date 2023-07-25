package ukidelly.api.v1.user.service

import org.koin.core.annotation.Module
import ukidelly.api.v1.user.models.UserLoginRequest
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.database.models.user.User
import ukidelly.database.models.user.UserDao
import ukidelly.database.models.user.UserEntity
import java.util.*

@Module
class UserService {

    val dao = UserDao()

    /**
     * 로그인
     * @param loginRequest 로그인 요청 Dto
     * @return [UserEntity?] 유저 정보
     */
    suspend fun login(loginRequest: UserLoginRequest): User? {

        return dao.getUserBySnsId(loginRequest.snsId)
    }


    /**
     * 회원가입
     * @param userRegisterRequest 회원가입 요청 Dto
     * @return [UserEntity]? 유저 정보로, 유저가 존재하면 [null]을 반환, 존재하지 않아 가입에 성공하면 [UserEntity]를 반환

     */
    suspend fun register(userRegisterRequest: UserRegisterRequest): User? = dao.addNewUser(userRegisterRequest)


}
