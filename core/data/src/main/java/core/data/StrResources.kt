package core.data

import android.content.Context
import androidx.annotation.StringRes

internal class StrResources(private val context: Context) {

    fun get(@StringRes id: Int): String {
        return context.resources.getString(id)
    }

    fun get(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return context.resources.getString(resId, *formatArgs)
    }
}