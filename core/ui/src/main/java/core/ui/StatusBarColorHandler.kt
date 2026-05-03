package core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun StatusBarColorHandler(backgroundColor: Color = MaterialTheme.colorScheme.background) {
    val statusBarBackgroundColorHandler = LocalStatusBarBackgroundColorHandler.current
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        statusBarBackgroundColorHandler.setColor(backgroundColor)

        if (backgroundColor.isDark) {
            context.setStatusBarContentLight()
        } else {
            context.setStatusBarContentDark()
        }
    }
}