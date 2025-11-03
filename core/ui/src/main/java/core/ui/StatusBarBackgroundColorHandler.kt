package core.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow

class StatusBarBackgroundColorHandler {
    val color = MutableStateFlow(Color.White)

    fun setColor(value: Color) {
        color.value = value
    }
}

val LocalStatusBarBackgroundColorHandler =
    compositionLocalOf<StatusBarBackgroundColorHandler> { error("No info is provided.") }