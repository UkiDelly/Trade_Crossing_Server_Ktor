package ukidelly.systems.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class ResponseDto<T>(

    @Transient
    open val message: String? = null,
) {

    @Serializable
    data class Success<T>(
        val data: T,
        override val message: String,
    ) : ResponseDto<T>()


    @Serializable
    data class Error<T>(
        val error: T,
        override val message: String,
    ) : ResponseDto<T>()
}


