package feature.movies.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import core.ui.component.InteractivePoster
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel()
) {
    val upcomingMovies = viewModel.upcomingMovies.collectAsLazyPagingItems()
    val moviesNowPlaying = viewModel.moviesNowPlaying.collectAsLazyPagingItems()
    val popularMovies = viewModel.popularMovies.collectAsLazyPagingItems()
    val topRatedMovies = viewModel.topRatedMovies.collectAsLazyPagingItems()

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        MovieListTitle(stringResource(R.string.upcoming))

        Movies(
            movies = upcomingMovies,
            navigateToDetails = {},
        )

        MovieListTitle(stringResource(R.string.now_playing))

        Movies(
            movies = moviesNowPlaying,
            navigateToDetails = {},
        )

        MovieListTitle(stringResource(R.string.popular))

        Movies(
            movies = popularMovies,
            navigateToDetails = {},
        )

        MovieListTitle(stringResource(R.string.top_rated))

        Movies(
            movies = topRatedMovies,
            navigateToDetails = {},
        )
    }
}

@Composable
private fun MovieListTitle(title: String) {
    Text(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp),
        text = title,
        color = Color.Gray,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun Movies(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<MovieUiModel>,
    navigateToDetails: (movieId: Int) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            if (movies.loadState.refresh == LoadState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
                    onClick = { navigateToDetails(movie.id) }
                )
            }
        }
    }
}
