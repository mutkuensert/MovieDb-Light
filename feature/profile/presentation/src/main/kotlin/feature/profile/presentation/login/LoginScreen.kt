package feature.profile.presentation.login

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.ui.StatusBarColorHandler
import core.ui.component.OneTimeEffect
import core.ui.component.PrimaryButton
import kotlinx.serialization.Serializable
import libraries.Constants.APP_DEEP_LINK
import libraries.R
import org.koin.androidx.compose.koinViewModel

const val LoginDeeplink = "${APP_DEEP_LINK}/login"

@Serializable
data class LoginRoute(val cameFromTmdbLogin: Boolean = false)

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val shouldOpenLoginWebPage by viewModel.shouldOpenLoginWebPage.collectAsStateWithLifecycle()
    val loggedIn by viewModel.loggedIn.collectAsStateWithLifecycle()

    OneTimeEffect {
        if (loggedIn) {
            viewModel.navigateToProfile()
        }
    }
    if (!loggedIn) {
        Login(shouldOpenLoginWebPage, viewModel::login, viewModel.requestToken)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.initScreen() }
    StatusBarColorHandler(MaterialTheme.colorScheme.background)
}

@Composable
private fun Login(
    shouldOpenLoginWebPage: Boolean,
    onLoginClick: () -> Unit,
    requestToken: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.weight(1f),
            painter = painterResource(R.drawable.tmdb_logo_blue_square),
            contentDescription = null
        )

        PrimaryButton(
            onClick = onLoginClick,
            text = stringResource(feature.profile.presentation.R.string.login),
            textColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        )
    }

    val context = LocalContext.current
    LaunchedEffect(shouldOpenLoginWebPage) {
        if (shouldOpenLoginWebPage) {
            launchLoginWebPage(
                requireNotNull(requestToken) { "Cannot be null if should open" },
                context
            )
        }
    }
}

private fun launchLoginWebPage(requestToken: String, context: Context) {
    val intent = CustomTabsIntent.Builder()
        .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
        .build()

    var uri = ("https://www.themoviedb.org/authenticate/" +
            requestToken +
            "?redirect_to" +
            "=$LoginDeeplink?${KEY_CAME_FROM_TMDB_LOGIN}=true").toUri()
    if (uri.scheme == null) {
        uri = uri
            .buildUpon()
            .scheme("https")
            .build()
    }

    intent.launchUrl(context, uri)
}
