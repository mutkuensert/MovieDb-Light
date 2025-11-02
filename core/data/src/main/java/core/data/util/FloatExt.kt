package core.data.util

import kotlin.math.pow
import kotlin.math.round

fun Float.withDecimals(n: Int): Float {
    val factor = 10.0f.pow(n)
    return round(this * factor) / factor
}