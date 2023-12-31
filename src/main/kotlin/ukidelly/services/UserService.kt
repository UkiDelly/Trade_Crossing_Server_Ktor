package ukidelly.services

import at.favre.lib.crypto.bcrypt.BCrypt
import org.koin.core.annotation.Single
import ukidelly.database.models.user.UserEntity
import ukidelly.dto.requests.UserRegisterRequestDto
import ukidelly.models.User
import ukidelly.repositories.UserRepository
import ukidelly.systems.errors.PasswordIncorrectException
import ukidelly.systems.errors.UserExistException
import ukidelly.systems.errors.UserNotExistException
import ukidelly.systems.models.LoginType
import java.util.*

@Single
class UserService(private val repository: UserRepository) {


    /**
     * 이메일 로그인
     * @param email 이메일
     * @param password 비밀번호
     * @return [User]? 유저정보
     */
    suspend fun emailLogin(email: String, password: String): User {

        val userEntity = repository.findEmailUser(email) ?: throw UserNotExistException()
        val isVerified = BCrypt.verifyer().verify(password.toCharArray(), userEntity.password).verified

        if (isVerified) {
            return User(userEntity)
        } else {
            throw PasswordIncorrectException()
        }
    }

    /**
     * 로그인
     * @param loginRequest 로그인 요청 Dto
     * @return [UserEntity]? 유저 정보
     */
    suspend fun socialLogin(snsId: String, email: String, loginType: LoginType): User {
        return repository.findSocialUser(
            snsId = snsId,
            email = email,
            loginType = loginType
        )?.let { User(it) } ?: throw UserNotExistException()

    }

    /**
     * 자동 로그인
     * @param userId 유저 아이디
     * @return [User] 유저 정보 또는 [null]
     *
     */
    suspend fun autoLogin(uuid: UUID): User {
        return repository.findUserByUUID(uuid)?.let { User(it) } ?: throw UserNotExistException()
    }

    /**
     * 회원가입
     * @param userRegisterRequest 회원가입 요청 Dto
     * @return [UserEntity]? 유저 정보로, 유저가 존재하면 [null]을 반환, 존재하지 않아 가입에 성공하면 [UserEntity]를 반환

     */
    suspend fun register(userRegisterRequest: UserRegisterRequestDto): User {

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
                } ?: User(repository.addNewUser(userRegisterRequest, hashedPassword))
            }

            else -> {
                repository.findSocialUser(
                    userRegisterRequest.snsId,
                    userRegisterRequest.email,
                    userRegisterRequest.loginType
                )?.let {
                    throw UserExistException("이미 가입한 유저입니다.")
                } ?: User(repository.addNewUser(userRegisterRequest))
            }
        }
    }

}
