package ukidelly.systems.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class ResponseDto(

    @Transient
    open val message: String? = null,
) {

    @Serializable
    data class Success<T>(
        val data: T,
        override val message: String,
    ) : ResponseDto()


    @Serializable
    data class Error(
        val error: String,
        override val message: String,
    ) : ResponseDto()
}


