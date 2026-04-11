package moviedblight.ui.home

import androidx.lifecycle.ViewModel
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import core.ui.StatusBarBackgroundColorHandler
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator

class HomeViewModel(
    private val navigator: Navigator,
    val loadingAnimator: LoadingAnimator,
    val popupHandler: PopupHandler,
    statusBarBackgroundColorHandler: StatusBarBackgroundColorHandler
) : ViewModel() {
    val statusBarContentColor = statusBarBackgroundColorHandler.color

    fun navigateToMovies(reselected: Boolean) {
        navigator.navigateToTab(NavTab.MovieTab, reselected)
    }

    fun navigateToTvShows(reselected: Boolean) {
        navigator.navigateToTab(NavTab.TvShowTab, reselected)
    }

    fun navigateToSearch(reselected: Boolean) {
        navigator.navigateToTab(NavTab.SearchTab, reselected)
    }

    fun navigateToProfile(reselected: Boolean) {
        navigator.navigateToTab(NavTab.ProfileTab, reselected)
    }

    fun closePopup() {
        popupHandler.close()
    }
}
