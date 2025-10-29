package feature.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.domain.AccountRepository
import core.domain.AuthState
import core.domain.AuthenticationRepository
import core.libraries.image.TmdbImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

const val KEY_CAME_FROM_TMDB_LOGIN = "cameFromTmdbLogin"

class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val accountRepository: AccountRepository,
    private val authState: AuthState,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var cameFromTmdbLogin: Boolean = savedStateHandle[KEY_CAME_FROM_TMDB_LOGIN] ?: false

    private var _shouldOpenLoginPage = MutableStateFlow(false)
    val shouldOpenLoginWebPage = _shouldOpenLoginPage.asStateFlow()

    val loggedIn: StateFlow<Boolean> get() = authState.loggedIn

    private val _uiModel = MutableStateFlow(ProfileUiModel.empty())
    val uiModel = _uiModel.asStateFlow()

    var requestToken: String? = null
        private set

    init {
        if (cameFromTmdbLogin) {
            _shouldOpenLoginPage.value = false
            requestToken = null
            startSession()
        } else {
            _shouldOpenLoginPage.value = false
        }

        viewModelScope.launch {
            loggedIn.collectLatest {
                if (it) {
                    val user = accountRepository.getUser()
                    _uiModel.update { model ->
                        model.copy(
                            profileImageUrl = user.profilePicturePath?.let { path ->
                                TmdbImage.Profile(path)
                            }?.w185Url,
                            name = user.userName
                        )
                    }
                }
            }
        }
    }

    private fun startSession() {
        viewModelScope.launch {
            authenticationRepository.startSession()
                .onSuccess {
                    Timber.i("Successfully logged in")
                }
                .onFailure {
                    Timber.w(it)
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
                .onFailure {
                    //Show error message
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.logout()
        }
    }
}