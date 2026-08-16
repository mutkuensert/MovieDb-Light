package filmcan.ui

import androidx.lifecycle.ViewModel
import com.mutkuensert.filmcan.R
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import filmcan.RemoteConfigApiKeyHandler
import utils.stringresource.StringResource
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val popupHandler: PopupHandler,
    private val stringResource: StringResource,
    private val navigator: Navigator,
    private val remoteConfigApiKeyHandler: RemoteConfigApiKeyHandler,
) : ViewModel() {

    fun handleSecurityPatchError() {
        popupHandler.show {
            message = stringResource.get(R.string.connection_is_not_secure)
            showConfirmButton = true
            onConfirm = navigator::closeApp
        }
    }

    fun setupRemoteConfigApiKey() {
        remoteConfigApiKeyHandler.initialize()
    }
}