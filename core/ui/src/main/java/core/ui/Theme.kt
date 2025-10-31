package core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue,
    secondary = LightBlue,
    tertiary = LightGreen
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    secondary = LightBlue,
    onSecondary = Color.White,
    tertiary = LightGreen,
    onTertiary = Color.White,
    surface = DarkBlue,
    onSurface = Color.White,
    background = DarkBlue,
    onBackground = Color.White
)

@Composable
fun MoviedbLightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        LightColorScheme//DarkColorScheme
    } else {
        LightColorScheme
    }

    /*val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (darkTheme) {
            context.setStatusBarContentLight()
        } else {
            context.setStatusBarContentDark()
        }
    }*/

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
