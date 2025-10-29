package core.libraries

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.core.view.WindowCompat

fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.findActivity()
        else -> null
    }
}

fun Context.setStatusBarContentDark() {
    val activity = findActivity() ?: return

    val insetsController =
        WindowCompat.getInsetsController(activity.window, activity.window.decorView)
    insetsController.isAppearanceLightStatusBars = true
}

fun Context.setStatusBarContentLight() {
    val activity = findActivity() ?: return

    val insetsController =
        WindowCompat.getInsetsController(activity.window, activity.window.decorView)
    insetsController.isAppearanceLightStatusBars = false
}