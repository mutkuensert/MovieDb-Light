package utils.stringresource

import androidx.annotation.StringRes

interface StrResource {
    fun get(@StringRes id: Int): String

    fun get(@StringRes resId: Int, vararg formatArgs: Any?): String
}