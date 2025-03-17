package core.ui.palette

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

val Bitmap.bodyTextColor: Color
    get() {
        val rgb = Palette.from(this)
            .generate()
            .dominantSwatch
            ?.bodyTextColor ?: 0xFFFFFF
        return Color(rgb)
    }

val Bitmap.titleTextColor: Color
    get() {
        val rgb = Palette.from(this)
            .generate()
            .dominantSwatch
            ?.titleTextColor ?: 0xFFFFFF
        return Color(rgb)
    }