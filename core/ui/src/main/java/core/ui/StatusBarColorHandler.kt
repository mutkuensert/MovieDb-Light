package core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import libraries.setStatusBarContentDark
import libraries.setStatusBarContentLight

@Composable
fun StatusBarColorHandler(backgroundColor: Color) {
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