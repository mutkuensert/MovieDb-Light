package core.ui.navigation

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

sealed interface NavTab {
    val tabConfig: TabConfig get() = TabConfig()

    companion object {
        val startDestination get() = MoviesRoute
        val allRoutes: List<String>
            get() = listOf(
                MoviesRoute::class.java.simpleName,
                ProfileRoute::class.java.simpleName
            )
    }

    @Serializable
    data object MoviesRoute : NavTab

    @Serializable
    data class ProfileRoute(val cameFromTmdbLogin: Boolean = false) : NavTab
}

class TabConfig(
    val selectedColor: Color = Color.Gray,
    val unselectedColor: Color = Color.DarkGray
)