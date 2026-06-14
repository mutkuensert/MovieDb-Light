package feature.splash.presentation

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.security.ProviderInstaller
import core.ui.FilmCanTheme
import core.ui.LightBlue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

const val SECURITY_PROVIDER_UPDATE_REQUEST_CODE = 5

@Composable
fun SplashScreen() {
    val viewModel = hiltViewModel<SplashViewModel>()

    Splash()

    val context = LocalContext.current
    val activity = LocalActivity.current
    LaunchedEffect(Unit) {
        if (activity == null) return@LaunchedEffect

        ProviderInstaller.installIfNeededAsync(
            context,
            object : ProviderInstaller.ProviderInstallListener {
                override fun onProviderInstalled() {
                    viewModel.handleSuccessfulSecurityProviderInstallation()
                }

                override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
                    val availability = GoogleApiAvailability.getInstance()
                    if (availability.isUserResolvableError(errorCode)) {
                        // Recoverable error. Show a dialog prompting the user to
                        // install/update/enable Google Play services.
                        availability.showErrorDialogFragment(
                            activity,
                            errorCode,
                            SECURITY_PROVIDER_UPDATE_REQUEST_CODE
                        ) {
                            viewModel.handleUserDeclinedSecurityPatch()
                        }
                    } else {
                        viewModel.handleNonRecoverableError()
                    }
                }
            })

    }
}

@Composable
private fun Splash() {
    val transition = rememberInfiniteTransition(label = "splash")
    val iconScale by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconScale"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.size(32.dp))

            Image(
                painter = painterResource(id = utils.R.drawable.app_icon),
                contentDescription = stringResource(R.string.app_icon),
                Modifier
                    .size(192.dp)
                    .scale(iconScale)
            )

            CircularProgressIndicator(
                color = LightBlue,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SplashPreview() {
    FilmCanTheme { Splash() }
}