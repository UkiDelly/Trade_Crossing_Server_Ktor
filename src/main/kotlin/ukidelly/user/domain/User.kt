package ukidelly.user.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.util.*

enum class LoginType {
    google, apple;

    @JsonValue
    override fun toString(): String {
        return super.toString().lowercase()
    }

    @JsonCreator
    fun fromString(value: String): LoginType? {
        return try {
            valueOf(value.lowercase())
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}

interface UserEntity : Entity<UserEntity> {
    companion object : Entity.Factory<UserEntity>()

    val userId: UUID
    var snsId: String
    var email: String
    var userName: String
    var islandName: String
    var introduction: String
    var loginType: LoginType
}

object UserTable : Table<UserEntity>("user_info") {
    val userId = uuid("user_id").primaryKey().bindTo { it.userId }
    val snsId = varchar("sns_id").bindTo { it.snsId }
    val email = varchar("email").bindTo { it.email }
    val userName = varchar("user_name").bindTo { it.userName }
    val islandName = varchar("island_name").bindTo { it.islandName }
    val introduction = text("introduction").bindTo { it.introduction }
    val loginType = enum<LoginType>("login_type").bindTo { it.loginType }

}
