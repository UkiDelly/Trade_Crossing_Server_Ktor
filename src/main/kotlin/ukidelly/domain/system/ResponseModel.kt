package ukidelly.domain.system

import kotlinx.serialization.Serializable

@Serializable
class ResponseModel<out T>(val data: T?, val message: String?)
