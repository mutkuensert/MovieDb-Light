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

    fun navigateToMovies() {
        navigator.navigateToTab(NavTab.MovieTab)
    }

    fun navigateToSearch() {
        navigator.navigateToTab(NavTab.SearchTab)
    }

    fun navigateToProfile() {
        navigator.navigateToTab(NavTab.ProfileTab)
    }

    fun closePopup() {
        popupHandler.close()
    }
}
