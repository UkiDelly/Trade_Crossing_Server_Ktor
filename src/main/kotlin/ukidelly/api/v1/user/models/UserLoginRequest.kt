package ukidelly.api.v1.user.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
data class SocialLoginRequest(val snsId: String, val email: String) {
	fun toJsonString(): String {
		return """{"sns_id": "$snsId", "email": "$email"}"""
	}
}

@Serializable
data class EmailLoginReqeust(val email: String, val password: String) {
	fun toJsonString(): String {
		return Json.encodeToString(this)
	}
}