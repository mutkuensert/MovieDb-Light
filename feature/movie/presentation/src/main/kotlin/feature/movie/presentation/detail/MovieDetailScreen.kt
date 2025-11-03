package feature.movie.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import core.ui.AppColors
import core.ui.MoviedbLightTheme
import core.ui.coil.debugPlaceholder
import core.ui.component.Poster
import core.ui.component.PosterSize
import feature.movie.presentation.R
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class MovieDetailRoute(val id: Int)

@Composable
fun MovieDetailScreen(viewModel: MovieDetailViewModel = koinViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    MovieDetail(uiModel)

    LaunchedEffect(Unit) {
        viewModel.getDetails()
    }
}

@Composable
private fun MovieDetail(uiModel: MovieDetailUiModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        var loadedImage by remember { mutableStateOf(false) }
        var loading by remember { mutableStateOf(true) }

        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uiModel.imageUrl)
                    .crossfade(true)
                    .allowHardware(true)
                    .build(),
                onLoading = { loading = true },
                onSuccess = {
                    loading = false
                    loadedImage = true
                },
                onError = { loading = false },
                error = debugPlaceholder(core.ui.R.drawable.debug_placeholder_dog),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(),
                contentDescription = stringResource(core.ui.R.string.image)
            )

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCB000000)
                            ),
                        )
                    )
            )

            if (loadedImage) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = uiModel.releaseDate,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )

                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.Star,
                        tint = AppColors.star,
                        contentDescription = stringResource(core.ui.R.string.vote_icon)
                    )

                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = uiModel.voteAverage,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )

                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = stringResource(R.string.runtime, uiModel.runtime),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }
            }
        }
        Column(Modifier.padding(horizontal = 16.dp)) {
            if (!loadedImage) {
                Row(
                    Modifier.padding(bottom = 4.dp, top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(end = 12.dp),
                        text = uiModel.releaseDate,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )

                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.Star,
                        tint = AppColors.star,
                        contentDescription = stringResource(core.ui.R.string.vote_icon)
                    )

                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = uiModel.voteAverage,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )

                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = stringResource(R.string.runtime, uiModel.runtime),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }
            }

            var isOverviewShrinked by remember { mutableStateOf(true) }

            Box(Modifier.clickable {
                isOverviewShrinked = !isOverviewShrinked
            }) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .then(if (isOverviewShrinked) Modifier.height(92.dp) else Modifier),
                    text = uiModel.overview,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (isOverviewShrinked) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .align(Alignment.BottomEnd)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background
                                    ),
                                )
                            )
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyRow {
            itemsIndexed(uiModel.cast) { index, person ->
                if (index == 0) {
                    Spacer(Modifier.width(4.dp))
                }
                Person(
                    person.name,
                    person.character,
                    person.imageUrl,
                    Modifier.padding(horizontal = 4.dp)
                )

                if (index == uiModel.cast.lastIndex) {
                    Spacer(Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
private fun Person(
    name: String,
    character: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Poster(url = imageUrl, size = PosterSize.Medium)

        Spacer(Modifier.height(4.dp))

        Box(modifier = Modifier.width(120.dp), contentAlignment = Alignment.TopCenter) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(4.dp))

        Box(modifier = Modifier.width(120.dp), contentAlignment = Alignment.TopCenter) {
            Text(
                text = character,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun MovieDetailPreview() {
    MoviedbLightTheme {
        MovieDetail(
            MovieDetailUiModel(
                imageUrl = null,
                title = "pharetra",
                voteAverage = "7.1",
                runtime = "120",
                releaseDate = "2010",
                overview = LoremIpsum(20).values.joinToString(" "),
                cast = listOf(
                    PersonUiModel(
                        id = 2722,
                        imageUrl = null,
                        name = "Some Person",
                        character = "recteque"
                    )
                )
            )
        )
    }
}