package ukidelly.utils

import io.ktor.server.plugins.*


fun String.toSnakeCase(): String {
    val result = StringBuilder()
    var prevIsUpperCase = false

    for (char in this) {
        prevIsUpperCase = if (char.isUpperCase() && !prevIsUpperCase) {
            result.append('_')
            result.append(char.lowercaseChar())
            true
        } else {
            result.append(char.lowercaseChar())
            char.isUpperCase()
        }
    }

    return result.toString()
}

object RequestUtils {

    /**
     * 해당 쿼리 파라미터가 존재하는지 확인한다.
     * @param queryParam 쿼리 파라미터
     * @param parameters 확인할 파라미터
     * @throws MissingRequestParameterException 쿼리 파라미터가 존재하지 않을 경우 발생한다.
     */
    fun checkQueryParamExist(queryParam: Set<String>, vararg parameters: String) {

        // check id the queryParam contains the parameters
        val missingParam = mutableListOf<String>()

        parameters.forEach { param ->
            if (!queryParam.contains(param)) {
                missingParam.add(param)
            }
        }

        if (missingParam.isNotEmpty()) {
            throw MissingRequestParameterException(missingParam.toString())
        }
    }


}