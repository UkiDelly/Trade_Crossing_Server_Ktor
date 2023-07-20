package ukidelly.domain.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.Document
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import ukidelly.plugins.ObjectIdSerializer


@Serializable
enum class LoginMethod { google, apple }

@Serializable
data class User(

    @BsonId
    @Contextual
    @Serializable(with = ObjectIdSerializer::class)
    val _id: ObjectId = ObjectId.get(),
    val snsId: String,
    val email: String,
    val userName: String,
    val islandName: String,
    val introduction: String,
    val loginMethod: LoginMethod,
    val likedPost: List<@Contextual @Serializable(with = ObjectIdSerializer::class) ObjectId> = emptyList(),
    val blockedUser: List<@Contextual @Serializable(with = ObjectIdSerializer::class) ObjectId> = emptyList(),
    val createdAt: String,
) {

    fun toDocument(): Document {
        return Document(
            mapOf(
                "_id" to _id,
                "snsId" to snsId,
                "email" to email,
                "userName" to userName,
                "islandName" to islandName,
                "introduction" to introduction,
                "loginMethod" to loginMethod.name,
                "likedPost" to likedPost,
                "blockedUser" to blockedUser,
                "createdAt" to createdAt,
            )
        )
    }

    companion object {
        fun fromDocument(document: Document): User {
            return User(
                _id = document.getObjectId("_id"),
                snsId = document.getString("snsId"),
                email = document.getString("email"),
                userName = document.getString("userName"),
                islandName = document.getString("islandName"),
                introduction = document.getString("introduction"),
                loginMethod = document.getString("loginMethod")
                    .let { loginMethod -> LoginMethod.entries.first { it.name == loginMethod } },
                likedPost = document.getList("likedPost", ObjectId::class.java, emptyList<ObjectId>()),
                blockedUser = document.getList("blockedUser", ObjectId::class.java, emptyList<ObjectId>()),
                createdAt = document.getString("createdAt"),
            )
        }


    }
}
