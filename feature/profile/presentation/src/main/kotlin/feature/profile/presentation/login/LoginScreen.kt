package feature.profile.presentation.login

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import core.ui.component.OneTimeEffect
import core.ui.component.PrimaryButton
import org.koin.androidx.compose.koinViewModel
import utils.Constants.APP_DEEP_LINK
import utils.R

const val LoginDeeplink = "${APP_DEEP_LINK}/login"

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val shouldOpenLoginWebPage by viewModel.shouldOpenLoginWebPage.collectAsStateWithLifecycle()
    val loggedIn by viewModel.loggedIn.collectAsStateWithLifecycle()

    OneTimeEffect {
        if (loggedIn) {
            viewModel.popUpToProfile()
        }
    }
    if (!loggedIn) {
        Login(
            shouldOpenLoginWebPage,
            viewModel::handleSettingsClick,
            viewModel::handleAboutClick,
            viewModel::login,
            onGetRequestToken = { viewModel.requestToken }
        )
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.initScreen() }
}

@Composable
private fun Login(
    shouldOpenLoginWebPage: Boolean,
    onClickSettings: () -> Unit,
    onClickAbout: () -> Unit,
    onClickLogin: () -> Unit,
    onGetRequestToken: () -> String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            onClickSettings = onClickSettings,
            onClickAbout = onClickAbout
        )

        Image(
            modifier = Modifier
                .weight(1f)
                .size(192.dp),
            painter = painterResource(R.drawable.app_icon),
            contentDescription = null
        )

        PrimaryButton(
            onClick = onClickLogin,
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
                requireNotNull(onGetRequestToken.invoke()) { "Cannot be null if should open" },
                context
            )
        }
    }
}

@Composable
private fun TopBar(
    onClickSettings: () -> Unit,
    onClickAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        TextButton(onClick = onClickAbout) {
            Text(
                text = stringResource(feature.profile.presentation.R.string.about),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(onClick = onClickSettings, Modifier.padding(end = 16.dp)) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(feature.profile.presentation.R.string.settings_button_icon),
                tint = MaterialTheme.colorScheme.onBackground
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
