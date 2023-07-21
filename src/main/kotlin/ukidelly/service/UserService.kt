package ukidelly.service

import com.mongodb.client.MongoCollection
import org.bson.Document
import org.koin.core.annotation.Module
import ukidelly.domain.user.User
import ukidelly.domain.user.UserLoginRequest
import ukidelly.repository.UserRepository

@Module
class UserService {

    private lateinit var userRepository: UserRepository


    fun setRepository(userRepository: UserRepository, collection: MongoCollection<Document>) {
        this.userRepository = userRepository
        userRepository.setCollection(collection)
    }


    suspend fun findOne(request: UserLoginRequest): User? {
        val userDoc = userRepository.findOne(request.snsId, request.email)
        return userDoc?.let { User.fromDocument(it) }
    }

    suspend fun register(user: User) {

        //check if user already exists
        val registeredUser = userRepository.findOne(user.snsId, user.email)

        if (registeredUser != null) {
            throw IllegalStateException()
        } else {
            userRepository.save(user.toDocument())
        }


    }


}
