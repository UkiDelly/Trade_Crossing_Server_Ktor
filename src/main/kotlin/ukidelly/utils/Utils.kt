package ukidelly.utils

import java.time.LocalDateTime
import java.util.*

object Utils {

    fun convertDateToLocalDate(date: Date): LocalDateTime =
        date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()

    fun convertLocalDateToDate(localDate: java.time.LocalDate): Date =
        Date.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant())
}