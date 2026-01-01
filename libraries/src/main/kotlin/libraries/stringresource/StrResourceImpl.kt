package libraries.stringresource

import android.content.Context
import androidx.annotation.StringRes

class StrResourceImpl(private val context: Context) : StrResource {

    override fun get(@StringRes id: Int): String {
        return context.resources.getString(id)
    }

    override fun get(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return context.resources.getString(resId, *formatArgs)
    }
}