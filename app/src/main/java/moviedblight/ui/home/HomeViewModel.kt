package moviedblight.ui.home

import androidx.lifecycle.ViewModel
import core.ui.StatusBarBackgroundColorManager
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator

class HomeViewModel(
    private val navigator: Navigator,
    statusBarBackgroundColorManager: StatusBarBackgroundColorManager
) : ViewModel() {
    val statusBarContentColor = statusBarBackgroundColorManager.color

    fun navigateToMovies() {
        navigator.navigateToTab(NavTab.MovieTab)
    }

    fun navigateToProfile() {
        navigator.navigateToTab(NavTab.ProfileTab)
    }
}
