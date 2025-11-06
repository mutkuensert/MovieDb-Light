package feature.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.domain.AuthState
import core.domain.AuthenticationRepository
import libraries.image.TmdbImage
import core.ui.PopupHandler
import feature.profile.domain.LogoutUseCase
import feature.profile.domain.StartSessionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val KEY_CAME_FROM_TMDB_LOGIN = "cameFromTmdbLogin"

class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val authState: AuthState,
    private val savedStateHandle: SavedStateHandle,
    private val popupHandler: PopupHandler,
    private val startSessionUseCase: StartSessionUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private var cameFromTmdbLogin: Boolean = savedStateHandle[KEY_CAME_FROM_TMDB_LOGIN] ?: false

    private var _shouldOpenLoginPage = MutableStateFlow(false)
    val shouldOpenLoginWebPage = _shouldOpenLoginPage.asStateFlow()

    val loggedIn: StateFlow<Boolean> get() = authState.loggedIn

    private val _uiModel = MutableStateFlow(ProfileUiModel.empty())
    val uiModel = _uiModel.asStateFlow()

    var requestToken: String? = null
        private set

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
            startSessionUseCase.execute().onSuccess { user ->
                _uiModel.update { model ->
                    model.copy(
                        profileImageUrl = user.profilePicturePath?.let { path ->
                            TmdbImage.Profile(path)
                        }?.w185Url,
                        name = user.userName
                    )
                }
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

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.execute()
        }
    }
}