package ukidelly.dto.requests

import kotlinx.serialization.Serializable
import ukidelly.systems.models.Currency
import ukidelly.systems.models.PostCategory


@Serializable
data class CreateTradeFeedRequestDto(
    val title: String,
    val content: String,
    val category: PostCategory,
    val currency: Currency,
    val price: Int?,
    val closed: Boolean = false,
)


@Serializable
data class UpdateTradeFeedRequestDto(
    val title: String?,
    val content: String?,
    val category: PostCategory?,
    val currency: Currency?,
    val price: Int?,
)