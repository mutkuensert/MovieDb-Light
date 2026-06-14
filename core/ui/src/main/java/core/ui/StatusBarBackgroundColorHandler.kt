package core.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatusBarBackgroundColorHandler @Inject constructor() {
    private val _color = MutableStateFlow(Color.White)
    val color = _color.asStateFlow()

    fun setColor(value: Color) {
        _color.value = value
    }
}

val LocalStatusBarBackgroundColorHandler =
    compositionLocalOf<StatusBarBackgroundColorHandler> { error("No info is provided.") }
