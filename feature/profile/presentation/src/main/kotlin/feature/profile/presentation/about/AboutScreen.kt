package feature.profile.presentation.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import core.ui.FilmCanTheme
import core.ui.addClickAction
import feature.profile.presentation.R

private const val TMDB_WEBSITE_URL = "https://www.themoviedb.org/"

@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current
    val supportNote = stringResource(R.string.tmdb_account_support_note)
    val website = stringResource(R.string.tmdb_website)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(220.dp),
            painter = painterResource(utils.R.drawable.tmdb_logo_blue_square),
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(R.string.tmdb_api_attribution),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = supportNote.addClickAction(
                startIndex = supportNote.indexOf(website),
                endIndex = supportNote.indexOf(website) + website.length,
                style = SpanStyle(color = Color(0xFFFF5722)),
                action = { uriHandler.openUri(TMDB_WEBSITE_URL) }
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    FilmCanTheme {
        AboutScreen()
    }
}
