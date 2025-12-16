package feature.profile.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.domain.AuthStateProvider
import core.domain.AuthenticationRepository
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.SettingsRoute
import feature.profile.domain.StartSessionUseCase
import feature.profile.presentation.profile.ProfileRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val KEY_CAME_FROM_TMDB_LOGIN = "cameFromTmdbLogin"

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val authStateProvider: AuthStateProvider,
    private val savedStateHandle: SavedStateHandle,
    private val popupHandler: PopupHandler,
    private val startSessionUseCase: StartSessionUseCase,
    private val navigator: Navigator,
) : ViewModel() {
    private var cameFromTmdbLogin: Boolean = savedStateHandle[KEY_CAME_FROM_TMDB_LOGIN] ?: false

    private var _shouldOpenLoginPage = MutableStateFlow(false)
    val shouldOpenLoginWebPage = _shouldOpenLoginPage.asStateFlow()

    val loggedIn: StateFlow<Boolean> get() = authStateProvider.loggedIn
    var requestToken: String? = null
        private set

    fun navigateToProfile() {
        navigator.navigateBack()
        navigator.navigateToRoute(ProfileRoute)
    }

    fun initScreen() {
        if (cameFromTmdbLogin) {
            _shouldOpenLoginPage.value = false
            requestToken = null
            startSession()
            savedStateHandle[KEY_CAME_FROM_TMDB_LOGIN] = false
            cameFromTmdbLogin = false
        } else {
            _shouldOpenLoginPage.value = false
        }
    }

    private fun startSession() {
        viewModelScope.launch {
            startSessionUseCase.execute().onSuccess {
                navigator.navigateBack()
                navigator.navigateToRoute(ProfileRoute)
            }.onFailure { errorMessage ->
                popupHandler.showSimpleMessage(errorMessage)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            authenticationRepository.getRequestToken()
                .onSuccess {
                    requestToken = it
                    _shouldOpenLoginPage.value = true
                }
                .onFailure { errorMessage ->
                    popupHandler.showSimpleMessage(errorMessage)
                }
        }
    }

    fun handleSettingsClick() {
        navigator.navigateToRoute(SettingsRoute)
    }
}