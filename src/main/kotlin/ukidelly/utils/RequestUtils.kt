package ukidelly.utils


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
//    val mapper = ObjectMapper()


    /**
     *  쿼리 파라미터를 클래스로 변환한다.
     */
//    inline fun <reified T : Any> Parameters.toClass(): T {
//        val map =
//            entries().associate {
//                it.key to (it.value.getOrNull(0) ?: throw IllegalArgumentException("${it.key}값이 유효하지 않습니다,"))
//            }
//
//        return mapper.convertValue(map, T::class.java)
//
//    }
//
//    inline fun <reified T : Any> ApplicationCall.getQueryParams(): T = request.queryParameters.toClass()
//
//    /**
//     * 해당 쿼리 파라미터가 존재하는지 확인한다.
//     * @param queryParam 쿼리 파라미터
//     * @param parameters 확인할 파라미터
//     * @throws MissingRequestParameterException 쿼리 파라미터가 존재하지 않을 경우 발생한다.
//     */
//    fun checkQueryParamExist(queryParam: Set<String>, vararg parameters: String) {
//
//        // check id the queryParam contains the parameters
//        val missingParam = mutableListOf<String>()
//
//        parameters.forEach { param ->
//            if (!queryParam.contains(param)) {
//                missingParam.add(param)
//            }
//        }
//
//        if (missingParam.isNotEmpty()) {
//            throw MissingRequestParameterException(missingParam.toString())
//        }
//    }


}