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

fun Context.asActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.asActivity()
        else -> null
    }
}

fun Context.getInsetsController(): WindowInsetsControllerCompat? {
    val activity = asActivity() ?: return null
    return WindowCompat.getInsetsController(
        activity.window,
        activity.window.decorView
    )
}

fun Context.setStatusBarAppearanceByDrawable(drawable: Drawable?) {
    if (drawable != null) {
        val dominantDrawableRgb = Palette.from(drawable.toBitmap())
            .generate()
            .dominantSwatch
            ?.rgb

        if (dominantDrawableRgb != null) {
            getInsetsController()?.isAppearanceLightStatusBars = !Color(dominantDrawableRgb).isDark
        }
    }
}