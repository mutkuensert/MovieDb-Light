package moviedblight.ui.home

import androidx.lifecycle.ViewModel
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator

class HomeViewModel(
    private val navigator: Navigator,
) : ViewModel() {

    fun navigateToMovies() {
        navigator.navigateToTab(NavTab.MoviesRoute)
    }

    fun navigateToSettings() {
        navigator.navigateToTab(NavTab.SettingsRoute)
    }
}
