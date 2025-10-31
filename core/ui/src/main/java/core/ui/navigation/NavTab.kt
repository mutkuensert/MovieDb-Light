package core.ui.navigation

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

sealed interface NavTab {
    val tabConfig: TabConfig get() = TabConfig()

    @Serializable
    object MovieTab : NavTab

    @Serializable
    object ProfileTab : NavTab
}

class TabConfig(
    val selectedColor: Color = Color.Gray,
    val unselectedColor: Color = Color.DarkGray
)