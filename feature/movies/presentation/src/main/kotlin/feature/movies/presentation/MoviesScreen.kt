package feature.movies.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel()
) {
    val popularMovies = viewModel.popularMovies.collectAsLazyPagingItems()

    PopularMovies(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        popularMovies = popularMovies,
        navigateToMovieDetails = {},
        onAddToFavorite = { isFavorite, movieId -> }
    )
}

@Composable
private fun PopularMovies(
    modifier: Modifier = Modifier,
    popularMovies: LazyPagingItems<MovieUiModel>,
    navigateToMovieDetails: (movieId: Int) -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(Modifier.height(10.dp))
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Popular Movies",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(10.dp))
            }
        }

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
                PopularMovie(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    id = movie.id,
                    title = movie.title,
                    imageUrl = movie.imageUrl,
                    isFavorite = null, //TODO
                    voteAverage = movie.voteAverage,
                    navigateToMovieDetails = { navigateToMovieDetails(movie.id) },
                    onAddToFavorite = onAddToFavorite
                )
            }
        }
    }
}

@Composable
private fun PopularMovie(
    modifier: Modifier = Modifier,
    id: Int,
    title: String,
    imageUrl: String?,
    isFavorite: Boolean?,
    voteAverage: Double,
    navigateToMovieDetails: () -> Unit,
    onAddToFavorite: (isFavorite: Boolean, movieId: Int) -> Unit,
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = navigateToMovieDetails),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Poster(modifier = Modifier.padding(5.dp), posterUrl = imageUrl)

        Spacer(Modifier.width(20.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = voteAverage.toString(),
                style = MaterialTheme.typography.bodyMedium
            )

            if (isFavorite != null) {
                Spacer(Modifier.height(10.dp))

                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = {
                        onAddToFavorite.invoke(!isFavorite, id)
                    }
                )
            }
        }
    }
}

@Preview(widthDp = 350)
@Composable
private fun PreviewPopularMoviesItem() {
    PopularMovie(
        id = 5363,
        title = "quis",
        imageUrl = null,
        isFavorite = null,
        voteAverage = 8.9,
        navigateToMovieDetails = {},
        onAddToFavorite = { _, _ -> }
    )
}
