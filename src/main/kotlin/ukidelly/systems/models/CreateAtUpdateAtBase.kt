package ukidelly.systems.models

import kotlinx.datetime.LocalDateTime


interface CreateAtUpdateAtBase {
    val createdAt: LocalDateTime
    val updatedAt: LocalDateTime
}
