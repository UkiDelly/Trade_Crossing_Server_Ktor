package ukidelly.api.v1.user.service

import at.favre.lib.crypto.bcrypt.BCrypt
import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject
import ukidelly.api.v1.user.models.User
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.api.v1.user.repository.UserRepository
import ukidelly.database.models.user.UserEntity
import ukidelly.systems.errors.PasswordIncorrectException
import ukidelly.systems.errors.UserExistException
import ukidelly.systems.models.LoginType
import java.util.*

@Module
class UserService {

    private val repository by inject<UserRepository>(clazz = UserRepository::class.java)


    /**
     * 이메일 로그인
     * @param email 이메일
     * @param password 비밀번호
     * @return [User]? 유저정보
     */
    suspend fun emailLogin(email: String, password: String): User? {

        val user = repository.findEmailUser(email) ?: return null
        val isVerified = BCrypt.verifyer().verify(password.toCharArray(), user.password).verified

        if (isVerified) {
            return user
        } else {
            throw PasswordIncorrectException()
        }
    }

    /**
     * 로그인
     * @param loginRequest 로그인 요청 Dto
     * @return [UserEntity]? 유저 정보
     */
    suspend fun socialLogin(snsId: String, email: String, loginType: LoginType): User? {
        return repository.findSocialUser(
            snsId = snsId,
            email = email,
            loginType = loginType
        )
    }

    /**
     * 자동 로그인
     * @param userId 유저 아이디
     * @return [User] 유저 정보 또는 [null]
     *
     */
    suspend fun autoLogin(userId: Int): User? {
        return repository.findUserById(userId)
    }

    /**
     * 회원가입
     * @param userRegisterRequest 회원가입 요청 Dto
     * @return [UserEntity]? 유저 정보로, 유저가 존재하면 [null]을 반환, 존재하지 않아 가입에 성공하면 [UserEntity]를 반환

     */
    suspend fun register(userRegisterRequest: UserRegisterRequest): User {

        repository.findUserByEmail(userRegisterRequest.email)?.let {
            throw UserExistException("${it.loginType}으로 가입한 유저입니다.")
        }

        return when (userRegisterRequest.loginType) {
            LoginType.email -> {
                val hashedPassword =
                    BCrypt.withDefaults().hashToString(12, userRegisterRequest.password!!.toCharArray())
                return repository.findEmailUser(
                    userRegisterRequest.email,
                )?.let {
                    throw UserExistException("이미 가입한 유저입니다.")
                } ?: repository.addNewUser(userRegisterRequest, hashedPassword)
            }

            else -> {
                repository.findSocialUser(
                    userRegisterRequest.snsId,
                    userRegisterRequest.email,
                    userRegisterRequest.loginType
                )?.let {
                    throw UserExistException("이미 가입한 유저입니다.")
                } ?: repository.addNewUser(userRegisterRequest)
            }
        }
    }

}
