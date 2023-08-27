package ukidelly.database.models.user

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ukidelly.api.v1.user.models.User
import java.util.*


class UserEntity(
	userId: EntityID<UUID>
) : UUIDEntity(userId) {
	companion object : UUIDEntityClass<UserEntity>(UserTable)

	var snsId by UserTable.snsId
	var email by UserTable.email
	var password by UserTable.password
	var defaultProfile by UserTable.defaultProfile
	var userName by UserTable.userName
	var islandName by UserTable.islandName
	var introduction by UserTable.introduction
	var loginType by UserTable.loginType


	fun toUser() = User(
		userId = id.value,
		snsId = snsId,
		email = email,
		password = password,
		profile = defaultProfile,
		userName = userName,
		islandName = islandName,
		introduction = introduction,
		loginType = loginType
	)

	override fun toString(): String {
		return "UserEntity(userId=${id.value}, snsId=$snsId, email=$email, password=$password, defaultProfile=$defaultProfile, " +
				"userName=$userName, islandName=$islandName, introduction=$introduction, loginType=$loginType)"
	}
}

