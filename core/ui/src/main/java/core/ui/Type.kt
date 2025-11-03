package core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography

fun getTypography(colorScheme: ColorScheme): Typography {
    val textColor = colorScheme.onBackground
    return Typography(
        bodyLarge = Typography().bodyLarge.copy(color = textColor),
        displayLarge = Typography().displayLarge.copy(color = textColor),
        displayMedium = Typography().displayMedium.copy(color = textColor),
        displaySmall = Typography().displaySmall.copy(color = textColor),
        headlineLarge = Typography().headlineLarge.copy(color = textColor),
        headlineMedium = Typography().headlineMedium.copy(color = textColor),
        headlineSmall = Typography().headlineSmall.copy(color = textColor),
        titleLarge = Typography().titleLarge.copy(color = textColor),
        titleMedium = Typography().titleMedium.copy(color = textColor),
        titleSmall = Typography().titleSmall.copy(color = textColor),
        bodyMedium = Typography().bodyMedium.copy(color = textColor),
        bodySmall = Typography().bodySmall.copy(color = textColor),
        labelLarge = Typography().labelLarge.copy(color = textColor),
        labelMedium = Typography().labelMedium.copy(color = textColor),
        labelSmall = Typography().labelSmall.copy(color = textColor),
    )
}