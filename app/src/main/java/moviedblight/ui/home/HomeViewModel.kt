package moviedblight.ui.home

import androidx.lifecycle.ViewModel
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import core.ui.StatusBarBackgroundColorHandler
import core.ui.navigation.Navigator
import feature.movie.presentation.list.MoviesRoute
import feature.profile.presentation.login.LoginRoute

class HomeViewModel(
    private val navigator: Navigator,
    val loadingAnimator: LoadingAnimator,
    val popupHandler: PopupHandler,
    statusBarBackgroundColorHandler: StatusBarBackgroundColorHandler
) : ViewModel() {
    val statusBarContentColor = statusBarBackgroundColorHandler.color

    fun navigateToMovies() {
        navigator.navigateToTab(MoviesRoute)
    }

    fun navigateToProfile() {
        navigator.navigateToTab(LoginRoute())
    }

    fun closePopup() {
        popupHandler.close()
    }
}
