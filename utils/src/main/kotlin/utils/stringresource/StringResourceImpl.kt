package utils.stringresource

import android.content.Context
import androidx.annotation.StringRes

class StringResourceImpl(private val context: Context) : StringResource {

    override fun get(@StringRes resId: Int): String {
        return context.resources.getString(resId)
    }

    override fun get(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return context.resources.getString(resId, *formatArgs)
    }
}