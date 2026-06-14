package utils.stringresource

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringResourceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : StringResource {

    override fun get(@StringRes resId: Int): String {
        return context.resources.getString(resId)
    }

    override fun get(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return context.resources.getString(resId, *formatArgs)
    }
}
