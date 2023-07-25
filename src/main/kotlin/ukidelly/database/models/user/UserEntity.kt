package ukidelly.database.models.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

enum class LoginType {
    google, apple;
}


// Entity
// UUIDEntity를 상속받는 User 클래스를 정의합니다.

@Serializable
data class User(

    @Contextual
    val userId: UUID,
    val snsId: String,
    val email: String,
    val userName: String,
    val islandName: String,
    val introduction: String,
    val loginType: LoginType
)

class UserEntity(
    userId: EntityID<UUID>,
) : UUIDEntity(userId) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var snsId by UserTable.snsId
    var email by UserTable.email
    var userName by UserTable.userName
    var islandName by UserTable.islandName
    var introduction by UserTable.introduction
    var loginType by UserTable.loginType

}


// Table
object UserTable : UUIDTable("user_info", "user_id") {

    val snsId = varchar("sns_id", 100)
    val email = varchar("email", 100)
    val userName = varchar("user_name", 100)
    val islandName = varchar("island_name", 100)
    val introduction = text("introduction")
    val loginType = enumerationByName<LoginType>("login_type", 100)

}

