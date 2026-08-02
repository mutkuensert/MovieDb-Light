package feature.splash.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import core.ui.PopupHandler
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator
import utils.stringresource.StringResource


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val navigator: Navigator,
    private val popupHandler: PopupHandler,
    private val stringResource: StringResource,
) : ViewModel() {

    fun handleSuccessfulSecurityProviderInstallation() {
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
