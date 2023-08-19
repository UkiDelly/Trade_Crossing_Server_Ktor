package ukidelly.database.models.user

import org.jetbrains.exposed.dao.id.UUIDTable
import ukidelly.systems.models.DefaultProfile
import ukidelly.systems.models.LoginType


// Table
object UserTable : UUIDTable("user_info", "user_id") {
	val snsId = varchar("sns_id", 100)
	val email = varchar("email", 100)
	val defaultProfile = enumerationByName<DefaultProfile>("default_profile", 10)
	val userName = varchar("user_name", 100)
	val islandName = varchar("island_name", 100)
	val introduction = text("introduction")
	val loginType = enumerationByName<LoginType>("login_type", 10)
}

