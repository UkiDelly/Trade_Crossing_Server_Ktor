package ukidelly.api.v1.user.service

import org.koin.core.annotation.Module
import org.koin.java.KoinJavaComponent.inject
import ukidelly.api.v1.user.models.User
import ukidelly.api.v1.user.models.UserRegisterRequest
import ukidelly.api.v1.user.repository.UserRepository
import ukidelly.database.models.user.UserEntity
import ukidelly.systems.models.LoginType
import java.util.*

@Module
class UserService {

	private val repository by inject<UserRepository>(clazz = UserRepository::class.java)

	/**
	 * 로그인
	 * @param loginRequest 로그인 요청 Dto
	 * @return [UserEntity?] 유저 정보
	 */
	suspend fun login(snsId: String, email: String, loginType: LoginType): User? {

		return repository.findUser(
			snsId = snsId,
			email = email,
			loginType = loginType
		)
	}


	/**
	 * 회원가입
	 * @param userRegisterRequest 회원가입 요청 Dto
	 * @return [UserEntity]? 유저 정보로, 유저가 존재하면 [null]을 반환, 존재하지 않아 가입에 성공하면 [UserEntity]를 반환

	 */
	suspend fun register(userRegisterRequest: UserRegisterRequest): User? {

		repository.findUser(userRegisterRequest.snsId, userRegisterRequest.email, userRegisterRequest.loginType)?.let {
			return null
		}
		return repository.addNewUser(userRegisterRequest)
	}


}
