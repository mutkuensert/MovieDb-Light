package feature.profile.presentation

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import core.libraries.Constants.APP_DEEP_LINK
import core.ui.MoviedbLightTheme
import core.ui.StatusBarColorHandler
import core.ui.component.PrimaryButton
import org.koin.androidx.compose.koinViewModel
import core.libraries.R as librariesR

const val ProfileDeeplink = "${APP_DEEP_LINK}/profile"

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val loggedIn by viewModel.loggedIn.collectAsStateWithLifecycle()
    val shouldOpenLoginWebPage by viewModel.shouldOpenLoginWebPage.collectAsStateWithLifecycle()
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    if (loggedIn) {
        LoggedInProfile(uiModel, viewModel::logout)
    } else {
        LoggedOutProfile(
            shouldOpenLoginWebPage,
            viewModel::login,
            viewModel.requestToken
        )
    }

    StatusBarColorHandler(MaterialTheme.colorScheme.primary)
}

@Composable
private fun LoggedOutProfile(
    shouldOpenLoginWebPage: Boolean,
    onLoginClick: () -> Unit,
    requestToken: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.weight(1f),
            painter = painterResource(librariesR.drawable.tmdb_logo_blue_square),
            contentDescription = null
        )

        PrimaryButton(
            onClick = onLoginClick,
            text = stringResource(R.string.login),
            textColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
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

@Composable
private fun LoggedInProfile(uiModel: ProfileUiModel, onLogoutClick: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        TopBar(uiModel.profileImageUrl, uiModel.name, onLogoutClick)
    }
}

@Composable
private fun TopBar(
    imageUrl: String?,
    text: String,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .clip(CircleShape)
                .size(40.dp)
                .background(Color.White)
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    imageUrl,
                    contentDescription = null
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Text(text = text, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)

        Spacer(Modifier.weight(1f))

        IconButton(onClick = onLogoutClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = stringResource(R.string.logout_button_icon),
                tint = MaterialTheme.colorScheme.onPrimary
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

@Preview
@Composable
private fun LoggedOutProfilePreview() {
    MoviedbLightTheme {
        LoggedOutProfile(
            false,
            {},
            null
        )
    }
}

@Preview
@Composable
private fun LoggedInProfilePreview() {
    MoviedbLightTheme {
        LoggedInProfile(
            ProfileUiModel(
                null,
                "Your name"
            ),
            {}
        )
    }
}
