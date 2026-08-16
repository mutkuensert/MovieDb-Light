package filmcan.ui

import androidx.lifecycle.ViewModel
import com.mutkuensert.filmcan.R
import core.domain.apikey.ApiKeyManager
import core.domain.apikey.ApiKeyState
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import filmcan.setupRemoteConfigUpdateListener
import utils.stringresource.StringResource
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val popupHandler: PopupHandler,
    private val stringResource: StringResource,
    private val navigator: Navigator,
    private val apiKeyManager: ApiKeyManager,
) : ViewModel() {

    fun handleSecurityPatchError() {
        popupHandler.show {
            message = stringResource.get(R.string.connection_is_not_secure)
            showConfirmButton = true
            onConfirm = navigator::closeApp
        }
    }

    fun setupRemoteConfigUpdateListener() {
        setupRemoteConfigUpdateListener(onUpdated = { apiKey: String ->
            apiKeyManager.tmdbApiKey.value = ApiKeyState.Success(apiKey)
        })
    }
}