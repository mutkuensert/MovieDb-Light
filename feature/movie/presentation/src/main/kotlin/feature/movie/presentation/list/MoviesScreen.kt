package feature.movie.presentation.list

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import core.ui.MoviedbLightTheme
import core.ui.StatusBarColorHandler
import core.ui.component.InteractivePoster
import core.ui.component.PosterHeight
import feature.movie.presentation.R
import feature.movie.presentation.list.model.MovieUiModel
import feature.movie.presentation.list.model.MoviesUiModel
import kotlinx.coroutines.flow.flowOf
import libraries.LocalizationHelper
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel()
) {
    val upcomingMovies = viewModel.upcomingMovies.collectAsLazyPagingItems()
    val moviesNowPlaying = viewModel.moviesNowPlaying.collectAsLazyPagingItems()
    val popularMovies = viewModel.popularMovies.collectAsLazyPagingItems()
    val topRatedMovies = viewModel.topRatedMovies.collectAsLazyPagingItems()
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    Movies(
        uiModel,
        upcomingMovies,
        moviesNowPlaying,
        popularMovies,
        topRatedMovies,
        viewModel::handleMovieClick,
        viewModel::handleOpenCountryDialogClick,
        viewModel::handleWatchlistClick,
        viewModel::handleDismissCountryDialog,
        viewModel::handleCountryClick
    )

    StatusBarColorHandler(MaterialTheme.colorScheme.background)
}

@Composable
private fun Movies(
    uiModel: MoviesUiModel,
    upcomingMovies: LazyPagingItems<MovieUiModel>,
    moviesNowPlaying: LazyPagingItems<MovieUiModel>,
    popularMovies: LazyPagingItems<MovieUiModel>,
    topRatedMovies: LazyPagingItems<MovieUiModel>,
    onClickMovie: (movieId: Int) -> Unit,
    onClickCountryDialog: () -> Unit,
    onClickWatchlist: (MovieUiModel) -> Unit,
    onDismissCountryDialog: () -> Unit,
    onClickCountry: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.padding(top = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))

            CountryButton(onClickCountryDialog, uiModel.selectedCountry)
        }

        MovieListTitle(stringResource(R.string.upcoming))

        Movies(
            movies = upcomingMovies,
            onClickMovie = onClickMovie,
            onWatchlistClick = onClickWatchlist
        )

        MovieListTitle(stringResource(R.string.now_playing))

        Movies(
            movies = moviesNowPlaying,
            onClickMovie = onClickMovie,
            onWatchlistClick = onClickWatchlist
        )

        MovieListTitle(stringResource(R.string.popular))

        Movies(
            movies = popularMovies,
            onClickMovie = onClickMovie,
            onWatchlistClick = onClickWatchlist
        )

        MovieListTitle(stringResource(R.string.top_rated))

        Movies(
            movies = topRatedMovies,
            onClickMovie = onClickMovie,
            onWatchlistClick = onClickWatchlist
        )
    }

    if (uiModel.isCountryDialogVisible) {
        CountryDialog(
            onDismiss = onDismissCountryDialog,
            onClickCountry = onClickCountry
        )
    }
}

@Composable
private fun CountryButton(
    onClick: () -> Unit,
    selectedCountry: String?
) {
    Row(
        Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Flag,
            contentDescription = stringResource(R.string.flag_icon),
            tint = MaterialTheme.colorScheme.primary
        )
        if (!selectedCountry.isNullOrEmpty()) {
            Text(
                modifier = Modifier.padding(start = 4.dp, end = 2.dp),
                text = selectedCountry,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun PreviewCountryButton() {
    Column {
        CountryButton({}, selectedCountry = "TR")
        CountryButton({}, selectedCountry = null)
    }
}

@Composable
fun CountryDialog(
    onDismiss: () -> Unit,
    onClickCountry: (country: String) -> Unit
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.select_a_country),
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Column(
                Modifier
                    .height(240.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val currentCountry by remember { derivedStateOf { LocalizationHelper.systemCountry } }
                TextButton({ onClickCountry.invoke(currentCountry) }) {
                    Text(
                        currentCountry,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                LocalizationHelper.allCountries.forEach { country ->
                    TextButton({ onClickCountry.invoke(country) }) {
                        Text(
                            country,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(onDismiss) {
                Text(
                    stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}

@Composable
private fun MovieListTitle(title: String) {
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun Movies(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<MovieUiModel>,
    onClickMovie: (movieId: Int) -> Unit,
    onWatchlistClick: (movie: MovieUiModel) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            if (movies.loadState.refresh == LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .height(PosterHeight.large)
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
                    modifier = Modifier
                        .height(PosterHeight.large)
                        .padding(horizontal = 6.dp, vertical = 10.dp)
                        .then(
                            when (index) {
                                0 -> Modifier.padding(start = 10.dp)
                                movies.itemCount - 1 if movies.loadState != LoadState.Loading -> {
                                    Modifier.padding(end = 10.dp)
                                }

                                else -> Modifier
                            }
                        ),
                    imagePath = movie.imagePath,
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

@Preview
@Composable
private fun MoviesScreenPreview() {
    MoviedbLightTheme {
        val emptyPagingData =
            flowOf(PagingData.from<MovieUiModel>(emptyList())).collectAsLazyPagingItems()
        MoviedbLightTheme {
            Movies(
                MoviesUiModel.initial().copy(selectedCountry = "TR"),
                emptyPagingData,
                emptyPagingData,
                emptyPagingData,
                emptyPagingData,
                {},
                {},
                {},
                {},
                {}
            )
        }
    }
}
