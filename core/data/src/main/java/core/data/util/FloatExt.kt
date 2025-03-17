package core.data.util

fun Float.withDecimals(n: Int): Float {
    return "%.${n}f".format(this).toFloat()
}