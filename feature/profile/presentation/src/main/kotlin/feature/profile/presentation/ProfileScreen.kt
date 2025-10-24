package feature.profile.presentation

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.libraries.Constants.APP_DEEP_LINK
import org.koin.androidx.compose.koinViewModel

const val ProfileDeeplink = "${APP_DEEP_LINK}/profile"

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val shouldOpenLoginWebPage by viewModel.shouldOpenLoginWebPage.collectAsStateWithLifecycle()

    TextButton(onClick = viewModel::login) {
        Text("Login")
    }

    val context = LocalContext.current
    LaunchedEffect(shouldOpenLoginWebPage) {
        if (shouldOpenLoginWebPage) {
            launchLoginWebPage(
                requireNotNull(viewModel.requestToken) { "Cannot be null if should open" },
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
            "=$ProfileDeeplink?$KEY_CAME_FROM_TMDB_LOGIN=true").toUri()
    if (uri.scheme == null) {
        uri = uri
            .buildUpon()
            .scheme("https")
            .build()
    }

    intent.launchUrl(context, uri)
}