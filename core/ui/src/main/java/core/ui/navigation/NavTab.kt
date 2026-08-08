package core.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavTab {

    @Serializable
    object MovieTab : NavTab

    @Serializable
    object TvShowTab : NavTab

    @Serializable
    object ProfileTab : NavTab

    @Serializable
    object SearchTab : NavTab

    @Serializable
    object AboutTab : NavTab
}
