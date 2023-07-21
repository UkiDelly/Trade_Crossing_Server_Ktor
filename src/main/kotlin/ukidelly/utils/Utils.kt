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