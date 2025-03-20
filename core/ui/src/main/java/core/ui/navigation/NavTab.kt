package core.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import androidx.navigation.serialization.generateHashCode
import kotlinx.serialization.Serializable

sealed interface NavTab {
    val navDestinationId: Int
    val tabConfig: TabConfig get() = TabConfig()

    companion object {
        val startDestination = MoviesRoute
        val all: List<NavTab> get() = listOf(MoviesRoute, SettingsRoute)
    }

    @Serializable
    data object MoviesRoute : NavTab {
        override val navDestinationId: Int
            @SuppressLint("RestrictedApi")
            get() = serializer().generateHashCode()
    }

    @Serializable
    data object SettingsRoute : NavTab {
        override val navDestinationId: Int
            @SuppressLint("RestrictedApi")
            get() = serializer().generateHashCode()
    }
}

class TabConfig(
    val selectedColor: Color = Color.Gray,
    val unselectedColor: Color = Color.DarkGray
)