package feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.ApiKeyManager
import core.domain.account.AccountRepository
import core.domain.auth.AuthStateProvider
import core.ui.PopupHandler
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import utils.Constants
import utils.stringresource.StringResource

class SplashViewModel(
    private val navigator: Navigator,
    private val popupHandler: PopupHandler,
    private val stringResource: StringResource,
    private val accountRepository: AccountRepository,
    private val authStateProvider: AuthStateProvider,
    private val apiKeyManager: ApiKeyManager,
) : ViewModel() {
    private val isApKeyFetched = MutableStateFlow(false)

    fun handleSuccessfulSecurityProviderInstallation() {
        viewModelScope.launch {
            withTimeout(Constants.TIMEOUT_MS) {
                isApKeyFetched.first { it } //Waits here until it's true
            }
            if (!isApKeyFetched.value) {
                popupHandler.show {
                    message = stringResource.get(R.string.something_is_wrong)
                    onConfirm = navigator::closeApp
                }
                return@launch
            }
            if (authStateProvider.loggedIn.value) {
                accountRepository.fetchWatchlistMovies()
                accountRepository.fetchWatchlistTvShows()
            }
            navigator.popUpToRoute(NavTab.MovieTab)
        }
    }

    fun handleSuccessfulRemoteConfigFetch(tmdbApiKey: String) {
        isApKeyFetched.value = true
        apiKeyManager.tmdbApiKey = tmdbApiKey
    }

    fun handleFailedRemoteConfigFetch() {
        isApKeyFetched.value = false
        popupHandler.show {
            message = stringResource.get(R.string.something_is_wrong)
            onConfirm = navigator::closeApp
        }
    }

    fun handleUpdatedRemoteConfigFetch(tmdbApiKey: String) {
        apiKeyManager.tmdbApiKey = tmdbApiKey
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
