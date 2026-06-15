package filmcan.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onOk
import core.data.auth.LogoutTrigger
import core.data.auth.LogoutTriggerEvent
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import core.ui.StatusBarBackgroundColorHandler
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator
import core.ui.route.LoginRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.profile.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    val loadingAnimator: LoadingAnimator,
    val popupHandler: PopupHandler,
    val logoutTrigger: LogoutTrigger,
    val logoutUseCase: LogoutUseCase,
    statusBarBackgroundColorHandler: StatusBarBackgroundColorHandler
) : ViewModel() {
    val statusBarContentColor = statusBarBackgroundColorHandler.color

    init {
        viewModelScope.launch {
            logoutTrigger.trigger.collectLatest { event ->
                if (event == LogoutTriggerEvent.LOGOUT) {
                    logoutUseCase.invoke().onOk {
                        popUpToLogin()
                        popupHandler.showSimpleMessage(filmcan.core.data.R.string.logged_out_unknown_reason)
                    }
                }
            }
        }
    }

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

    fun popUpToLogin() {
        navigator.popUpToRoute(LoginRoute())
    }
}
