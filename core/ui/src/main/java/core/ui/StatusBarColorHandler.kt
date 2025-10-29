package core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import core.libraries.setStatusBarContentDark
import core.libraries.setStatusBarContentLight

@Composable
fun StatusBarColorHandler(backgroundColor: Color) {
    val statusBarBackgroundColorManager = LocalStatusBarBackgroundColorManager.current
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        statusBarBackgroundColorManager.setColor(backgroundColor)

        if (backgroundColor.isDark) {
            context.setStatusBarContentLight()
        } else {
            context.setStatusBarContentDark()
        }
    }
}