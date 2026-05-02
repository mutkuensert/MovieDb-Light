package filmcan.ui

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.security.ProviderInstaller
import core.ui.LocalStatusBarBackgroundColorHandler
import core.ui.FilmCanTheme
import core.ui.StatusBarBackgroundColorHandler
import core.ui.navigation.Navigator
import filmcan.ui.home.HomeScreen
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

internal const val SECURITY_PROVIDER_UPDATE_REQUEST_CODE = 5

class MainActivity : ComponentActivity() {
    private val navigator: Navigator by inject()
    private val statusBarBackgroundColorHandler: StatusBarBackgroundColorHandler by inject()
    private val viewModel: MainViewModel by viewModel()
    private var retrySecurityProviderInstall = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalStatusBarBackgroundColorHandler provides statusBarBackgroundColorHandler) {
                FilmCanTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HomeScreen(navigator)
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
