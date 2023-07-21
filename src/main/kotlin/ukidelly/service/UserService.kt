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


    fun findOne(request: UserLoginRequest): User? {
        val userDoc = userRepository.findOne(request.snsId, request.email)
        return userDoc?.let { User.fromDocument(it) }

    }


}
