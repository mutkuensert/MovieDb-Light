package feature.movie.presentation.detail

import androidx.browser.customtabs.CustomTabsIntent
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import core.ui.AppColors
import core.ui.MoviedbLightTheme
import core.ui.coil.debugPlaceholder
import core.ui.component.InteractivePoster
import core.ui.component.OneTimeEffect
import core.ui.component.Poster
import core.ui.component.PosterSize
import core.ui.component.PrimaryButton
import feature.movie.presentation.R
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class MovieDetailRoute(val id: Int)

@Composable
fun MovieDetailScreen(viewModel: MovieDetailViewModel = koinViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()
    val similarMovies = viewModel.similarMovies.collectAsLazyPagingItems()

    MovieDetail(
        uiModel,
        viewModel::handleStreamServicesInfoButton,
        similarMovies,
        viewModel::handleMovieClick,
        viewModel::handleWatchlistClick,
    )

    OneTimeEffect {
        viewModel.getDetails()
    }
}

@Composable
private fun MovieDetail(
    uiModel: MovieDetailUiModel,
    onClickStreamingServicesInfoButton: () -> Unit,
    similarMovies: LazyPagingItems<MovieUiModel>,
    onClickMovie: (movieId: Int) -> Unit,
    onClickWatchlist: (MovieUiModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        MoviePoster(uiModel.imageUrl, uiModel.year, uiModel.vote, uiModel.runtime)

        Column(Modifier.padding(horizontal = 16.dp)) {
            Providers(
                uiModel.providerLogoUrls,
                onClickStreamingServicesInfoButton,
                Modifier.padding(top = 4.dp)
            )
            Overview(uiModel.overview, Modifier.padding(top = 8.dp))
            if (uiModel.trailerUrl != null) {
                val context = LocalContext.current
                PrimaryButton(
                    {
                        val intent = CustomTabsIntent.Builder()
                            .setShareState(CustomTabsIntent.SHARE_STATE_ON)
                            .build()
                        intent.launchUrl(context, uiModel.trailerUrl.toUri())
                    },
                    stringResource(R.string.trailer),
                    Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Cast(uiModel)

        if (similarMovies.itemCount != 0) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                text = stringResource(R.string.similar),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }

        SimilarMovies(similarMovies, onClickMovie, onClickWatchlist)
    }
}

@Composable
private fun MoviePoster(
    imageUrl: String?,
    year: String,
    vote: String,
    runtime: String,
    modifier: Modifier = Modifier,
) {
    var loading by remember { mutableStateOf(true) }

    Box(modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .allowHardware(true)
                .build(),
            onLoading = { loading = true },
            onSuccess = { loading = false },
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
        YearVoteRuntimeText(
            Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 2.dp),
            year,
            vote,
            runtime
        )
    }
}

@Composable
private fun Overview(overview: String, modifier: Modifier = Modifier) {
    var isOverviewShrinked by remember { mutableStateOf(true) }

    Box(modifier.clickable {
        isOverviewShrinked = !isOverviewShrinked
    }) {
        Text(
            modifier = Modifier
                .then(if (isOverviewShrinked) Modifier.height(92.dp) else Modifier),
            text = overview,
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

@Composable
private fun Providers(
    providerLogoUrls: List<String>,
    onClickStreamingServicesInfoButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        providerLogoUrls.forEachIndexed { index, logoUrl ->
            AsyncImage(
                model = logoUrl,
                error = debugPlaceholder(libraries.R.drawable.tmdb_logo_blue_square),
                modifier = Modifier
                    .height(36.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                contentDescription = stringResource(core.ui.R.string.image)
            )
            if (index != providerLogoUrls.lastIndex) {
                Spacer(Modifier.width(4.dp))
            }
        }

        if (providerLogoUrls.isNotEmpty()) {
            IconButton(onClickStreamingServicesInfoButton, Modifier.padding(start = 2.dp)) {
                Icon(
                    modifier = Modifier.height(24.dp),
                    imageVector = Icons.Filled.Info,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(R.string.streaming_services_info_button_icon)
                )
            }
        }
    }
}

@Composable
private fun Cast(uiModel: MovieDetailUiModel, modifier: Modifier = Modifier) {
    LazyRow(modifier.height(260.dp)) {
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

@Composable
private fun SimilarMovies(
    movies: LazyPagingItems<MovieUiModel>,
    onClickMovie: (movieId: Int) -> Unit,
    onWatchlistClick: (movie: MovieUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            if (movies.loadState.refresh == LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .height(PosterSize.Large.height)
                        .fillParentMaxWidth(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
        }

        items(
            count = movies.itemCount,
            key = movies.itemKey { it.id }
        ) { index ->
            val movie = movies[index]

            if (movie != null) {
                InteractivePoster(
                    modifier = Modifier.padding(10.dp),
                    url = movie.imageUrl,
                    title = movie.title,
                    vote = movie.voteAverage,
                    onPosterClick = { onClickMovie(movie.id) },
                    inWatchlist = movie.inWatchlist,
                    onWatchlistClick = { onWatchlistClick(movie) }
                )
            }
        }
    }
}

@Composable
private fun YearVoteRuntimeText(
    modifier: Modifier = Modifier,
    year: String,
    vote: String,
    runtime: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = year,
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
            text = vote,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )

        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = stringResource(R.string.runtime, runtime),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
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
        Poster(url = imageUrl, posterSize = PosterSize.Medium)

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
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun MovieDetailPreview() {
    MoviedbLightTheme {
        val emptyPagingData =
            flowOf(PagingData.from<MovieUiModel>(emptyList())).collectAsLazyPagingItems()

        MovieDetail(
            MovieDetailUiModel(
                imageUrl = null,
                title = "pharetra",
                vote = "7.1",
                runtime = "120",
                year = "2010",
                overview = LoremIpsum(20).values.joinToString(" "),
                providerLogoUrls = listOf("path", "path2"),
                trailerUrl = "123",
                cast = listOf(
                    PersonUiModel(
                        id = 2722,
                        imageUrl = null,
                        name = "Some Person",
                        character = "recteque"
                    )
                )
            ),
            {},
            emptyPagingData,
            {},
            {},
        )
    }
}