package ukidelly.systems.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class ResponseDto<T>(

    @Transient
    open val message: String = "",
)

@Serializable
data class SuccessResponseDto<T>(
    val data: T,
    override val message: String,
) : ResponseDto<T>(message)


@Serializable
data class ErrorResponseDto<T>(
    val error: T,
    override val message: String,
) : ResponseDto<T>(message)