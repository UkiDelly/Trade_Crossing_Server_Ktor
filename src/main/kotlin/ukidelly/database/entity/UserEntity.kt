package ukidelly.database.models.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class UserEntity(
    id: EntityID<Int>
) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)

    var uuid by Users.uuid
    var snsId by Users.snsId
    var email by Users.email
    var password by Users.password
    var defaultProfile by Users.defaultProfile
    var userName by Users.userName
    var islandName by Users.islandName
    var introduction by Users.introduction
    var loginType by Users.loginType


    override fun toString(): String {
        return "UserEntity(userId=${id.value}, snsId=$snsId, email=$email, password=$password, defaultProfile=$defaultProfile, " +
                "userName=$userName, islandName=$islandName, introduction=$introduction, loginType=$loginType)"
    }
}

