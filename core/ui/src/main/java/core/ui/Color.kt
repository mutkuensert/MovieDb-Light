package core.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val DarkBlue = Color(0xff0d253f)
val LightBlue = Color(0xff01b4e4)
val LightGreen = Color(0xff90cea1)

val ColdWhite = Color(0xFFF5F7FA)

val DisabledGray = Color(0xffe4e4e4)

val Color.isDark: Boolean get() = ColorUtils.calculateLuminance(this.toArgb()) < 0.1

object AppColors {
    val star = Color(0xFFFFC107)
}