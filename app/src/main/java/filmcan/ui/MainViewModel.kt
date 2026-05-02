package filmcan.ui

import androidx.lifecycle.ViewModel
import com.mutkuensert.filmcan.R
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import utils.stringresource.StringResource

class MainViewModel(
    private val popupHandler: PopupHandler,
    private val stringResource: StringResource,
    private val navigator: Navigator,
) : ViewModel() {

    fun handleSecurityPatchError() {
        popupHandler.show {
            message = stringResource.get(R.string.connection_is_not_secure)
            showConfirmButton = true
            onConfirm = navigator::closeApp
        }
    }
}