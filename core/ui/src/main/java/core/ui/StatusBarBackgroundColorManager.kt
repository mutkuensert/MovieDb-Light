package core.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow

class StatusBarBackgroundColorManager {
    val color = MutableStateFlow(Color.White)

    fun setColor(value: Color) {
        color.value = value
    }
}

val LocalStatusBarBackgroundColorManager =
    compositionLocalOf<StatusBarBackgroundColorManager> { error("No info is provided.") }