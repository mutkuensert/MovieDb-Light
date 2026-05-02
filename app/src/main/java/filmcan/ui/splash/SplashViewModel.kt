package filmcan.ui.splash

import androidx.lifecycle.ViewModel
import com.mutkuensert.filmcan.R
import core.ui.PopupHandler
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator
import utils.stringresource.StringResource

class SplashViewModel(
    private val navigator: Navigator,
    private val popupHandler: PopupHandler,
    private val stringResource: StringResource,
) : ViewModel() {

    fun navigateInsideApp() {
        navigator.popUpToRoute(NavTab.MovieTab)
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
