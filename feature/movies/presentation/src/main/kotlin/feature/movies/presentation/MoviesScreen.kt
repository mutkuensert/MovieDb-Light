package feature.movies.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val popularMovies = viewModel.popularMovies.collectAsLazyPagingItems()

    PopularMovies(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        popularMovies = popularMovies,
        navigateToMovieDetails = {},
    )
}

@Composable
private fun PopularMovies(
    modifier: Modifier = Modifier,
    popularMovies: LazyPagingItems<MovieUiModel>,
    navigateToMovieDetails: (movieId: Int) -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = stringResource(R.string.popular_movies),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(10.dp))
        }

        LazyRow(
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                if (popularMovies.loadState.refresh == LoadState.Loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
            }

            items(count = popularMovies.itemCount) { index ->
                val movie = popularMovies[index]

                if (movie != null) {
                    InteractivePoster(
                        modifier = Modifier.padding(10.dp),
                        url = movie.imageUrl,
                        title = movie.title,
                        description = null,
                        vote = movie.voteAverage,
                        onClick = { navigateToMovieDetails(movie.id) }
                    )
                }
            }
        }
    }
}
