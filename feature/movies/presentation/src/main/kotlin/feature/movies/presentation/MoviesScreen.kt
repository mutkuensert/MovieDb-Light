package feature.movies.presentation

import androidx.compose.foundation.background
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import core.libraries.CountryManager
import core.ui.MoviedbLightTheme
import core.ui.StatusBarColorHandler
import core.ui.component.InteractivePoster
import core.ui.component.PosterSize
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object MoviesRoute

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
        viewModel::handleOpenCountryDialogClick,
        viewModel::handleFavoriteClick,
        viewModel::handleDismissCountryDialog,
        viewModel::handleCountryClick
    )

    StatusBarColorHandler(MaterialTheme.colorScheme.surface)
}

@Composable
private fun Movies(
    uiModel: MoviesUiModel,
    upcomingMovies: LazyPagingItems<MovieUiModel>,
    moviesNowPlaying: LazyPagingItems<MovieUiModel>,
    popularMovies: LazyPagingItems<MovieUiModel>,
    topRatedMovies: LazyPagingItems<MovieUiModel>,
    handleOpenCountryDialogClick: () -> Unit,
    handleFavoriteClick: (MovieUiModel) -> Unit,
    handleDismissCountryDialog: () -> Unit,
    handleCountryClick: (String) -> Unit,
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

            CountryButton(handleOpenCountryDialogClick, uiModel.selectedCountry)
        }

        MovieListTitle(stringResource(R.string.upcoming))

        Movies(
            movies = upcomingMovies,
            navigateToDetails = {},
            onFavoriteClick = handleFavoriteClick
        )

        MovieListTitle(stringResource(R.string.now_playing))

        Movies(
            movies = moviesNowPlaying,
            navigateToDetails = {},
            onFavoriteClick = handleFavoriteClick
        )

        MovieListTitle(stringResource(R.string.popular))

        Movies(
            movies = popularMovies,
            navigateToDetails = {},
            onFavoriteClick = handleFavoriteClick
        )

        MovieListTitle(stringResource(R.string.top_rated))

        Movies(
            movies = topRatedMovies,
            navigateToDetails = {},
            onFavoriteClick = handleFavoriteClick
        )
    }

    if (uiModel.isCountryDialogVisible) {
        CountryDialog(
            onDismiss = handleDismissCountryDialog,
            onClickCountry = handleCountryClick
        )
    }
}

@Composable
private fun CountryButton(
    onClick: () -> Unit,
    selectedCountry: String?
) {
    IconButton(onClick) {
        Icon(
            imageVector = Icons.Filled.Flag,
            contentDescription = stringResource(R.string.flag_icon),
            tint = MaterialTheme.colorScheme.primary
        )

        if (!selectedCountry.isNullOrEmpty()) {
            Text(
                modifier = Modifier.padding(start = 4.dp, end = 2.dp),
                text = selectedCountry,
                style = TextStyle(fontWeight = FontWeight.Bold)
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
                val currentCountry by remember { derivedStateOf { CountryManager.current } }
                TextButton({ onClickCountry.invoke(currentCountry) }) {
                    Text(
                        currentCountry,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                CountryManager.all.forEach { country ->
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
        modifier = Modifier.padding(start = 20.dp, top = 4.dp),
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun Movies(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<MovieUiModel>,
    navigateToDetails: (movieId: Int) -> Unit,
    onFavoriteClick: (movie: MovieUiModel) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            if (movies.loadState.refresh == LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .height(PosterSize.Big.height)
                        .fillParentMaxWidth(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
        }

        items(count = movies.itemCount) { index ->
            val movie = movies[index]

            if (movie != null) {
                InteractivePoster(
                    modifier = Modifier.padding(10.dp),
                    url = movie.imageUrl,
                    title = movie.title,
                    description = null,
                    vote = movie.voteAverage,
                    onPosterClick = { navigateToDetails(movie.id) },
                    isFavorite = movie.isFavorite,
                    onFavoriteClick = { onFavoriteClick(movie) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun MoviesScreenPreview() {
    MoviedbLightTheme {
        val emptyData =
            flowOf(PagingData.from<MovieUiModel>(emptyList())).collectAsLazyPagingItems()
        Movies(
            MoviesUiModel.initial(),
            emptyData,
            emptyData,
            emptyData,
            emptyData,
            {},
            {},
            {},
            {})
    }
}
