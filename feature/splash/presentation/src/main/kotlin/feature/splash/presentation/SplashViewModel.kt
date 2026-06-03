package feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.account.AccountRepository
import core.domain.auth.AuthStateProvider
import core.ui.PopupHandler
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator
import kotlinx.coroutines.launch
import utils.stringresource.StringResource

class SplashViewModel(
    private val navigator: Navigator,
    private val popupHandler: PopupHandler,
    private val stringResource: StringResource,
    private val accountRepository: AccountRepository,
    private val authStateProvider: AuthStateProvider,
) : ViewModel() {

    fun handleSuccessfulSecurityProviderInstallation() {
        viewModelScope.launch {
            if (authStateProvider.loggedIn.value) {
                accountRepository.fetchWatchlistMovies()
                accountRepository.fetchWatchlistTvShows()
            }
            navigator.popUpToRoute(NavTab.MovieTab)
        }
    }

    fun handleUserDeclinedSecurityPatch() {
        popupHandler.show {
            message = stringResource.get(R.string.connection_is_not_secure)
            showConfirmButton = true
            onConfirm = navigator::closeApp
        }
    }

    fun handleNonRecoverableError() {
        popupHandler.show {
            message = stringResource.get(R.string.connection_is_not_secure)
            showConfirmButton = true
            onConfirm = navigator::closeApp
        }
    }
}
