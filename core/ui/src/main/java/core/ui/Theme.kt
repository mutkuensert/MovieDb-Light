package core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue,
    secondary = LightBlue,
    tertiary = LightGreen
)

private val LightColorScheme = lightColorScheme(
    primary = ColdWhite,
    onPrimary = Color.Black,
    secondary = LightBlue,
    onSecondary = ColdWhite,
    tertiary = LightGreen,
    onTertiary = ColdWhite,
    surface = DarkBlue,
    onSurface = ColdWhite,
    background = DarkBlue,
    onBackground = ColdWhite
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

    CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground)) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
