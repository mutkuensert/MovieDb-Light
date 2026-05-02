package utils.stringresource

import androidx.annotation.StringRes

interface StringResource {
    fun get(@StringRes resId: Int): String

    fun get(@StringRes resId: Int, vararg formatArgs: Any?): String
}