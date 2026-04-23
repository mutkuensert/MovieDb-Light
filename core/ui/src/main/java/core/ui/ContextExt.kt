package core.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.palette.graphics.Palette

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

fun Context.getInsetsController(): WindowInsetsControllerCompat? {
    val activity = findActivity() ?: return null
    return WindowCompat.getInsetsController(
        activity.window,
        activity.window.decorView
    )
}

fun Context.setStatusBarAppearanceByDrawable(drawable: Drawable) {
    val dominantDrawableRgb = Palette.from(drawable.toBitmap())
        .generate()
        .dominantSwatch
        ?.rgb

    if (dominantDrawableRgb != null) {
        getInsetsController()?.isAppearanceLightStatusBars = !Color(dominantDrawableRgb).isDark
    }
}