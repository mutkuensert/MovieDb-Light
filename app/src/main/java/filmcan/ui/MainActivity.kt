package filmcan.ui

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.security.ProviderInstaller
import core.ui.FilmCanTheme
import core.ui.LocalStatusBarBackgroundColorHandler
import core.ui.StatusBarBackgroundColorHandler
import core.ui.StatusBarColorHandler
import core.ui.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import feature.splash.presentation.SECURITY_PROVIDER_UPDATE_REQUEST_CODE
import filmcan.ui.home.HomeScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var statusBarBackgroundColorHandler: StatusBarBackgroundColorHandler

    private val viewModel: MainViewModel by viewModels()
    private var retrySecurityProviderInstall = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        viewModel.setupRemoteConfigApiKey()
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalStatusBarBackgroundColorHandler provides statusBarBackgroundColorHandler) {
                FilmCanTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HomeScreen(navigator)
                        StatusBarColorHandler()
                    }
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode == SECURITY_PROVIDER_UPDATE_REQUEST_CODE) {
            retrySecurityProviderInstall = true
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchApiKeyIfNotFetched()
    }

    override fun onPostResume() {
        super.onPostResume()
        ProviderInstaller.installIfNeededAsync(
            this,
            object : ProviderInstaller.ProviderInstallListener {
                override fun onProviderInstalled() {
                    //Successfully installed, nothing to do
                }

                override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
                    viewModel.handleSecurityPatchError()
                }
            })
    }
}
