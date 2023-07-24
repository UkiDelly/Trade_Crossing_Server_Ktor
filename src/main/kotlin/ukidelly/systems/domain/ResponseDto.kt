package ukidelly.systems.domain

sealed class ResponseDto<T>(

    open val message: String,
)

data class SuccessResponseDto<T>(
    val data: T,
    override val message: String,
) : ResponseDto<T>(message)


data class ErrorResponseDto<T>(
    val error: T,
    override val message: String,
) : ResponseDto<T>(message)