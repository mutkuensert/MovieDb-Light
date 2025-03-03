package core.ui.navigation

import androidx.compose.ui.graphics.Color

interface NavTab {
    val navDestinationId: Int
    val tabConfig: TabConfig get() = TabConfig()
}

class TabConfig(
    val selectedColor: Color = Color.Gray,
    val unselectedColor: Color = Color.DarkGray
)