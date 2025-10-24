package core.libraries

import android.content.Context
import androidx.annotation.StringRes

class StrResources(private val context: Context) {

    fun get(@StringRes id: Int): String {
        return context.resources.getString(id)
    }

    fun get(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return context.resources.getString(resId, *formatArgs)
    }
}