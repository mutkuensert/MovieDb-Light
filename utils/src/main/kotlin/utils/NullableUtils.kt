package utils

fun <T> T?.requireNotNull(message: String): T {
    return requireNotNull(this) { message }
}
