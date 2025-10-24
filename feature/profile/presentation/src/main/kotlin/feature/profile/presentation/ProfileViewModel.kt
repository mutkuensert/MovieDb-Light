package feature.profile.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.domain.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

const val KEY_CAME_FROM_TMDB_LOGIN = "cameFromTmdbLogin"

class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var cameFromTmdbLogin: Boolean = savedStateHandle[KEY_CAME_FROM_TMDB_LOGIN] ?: false

    private var _shouldOpenLoginPage = MutableStateFlow(false)
    val shouldOpenLoginWebPage = _shouldOpenLoginPage.asStateFlow()

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
}