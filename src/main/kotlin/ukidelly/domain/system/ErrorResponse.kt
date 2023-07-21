package ukidelly.domain.system

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseModel<T>(
    val error: T,
    val message: String
)
