package feature.movie.presentation.list

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import core.ui.FilmCanTheme
import core.ui.component.InteractivePoster
import feature.movie.presentation.R
import feature.movie.presentation.list.model.MovieUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = koinViewModel()
) {
    Movies(
        viewModel.upcomingMovies,
        viewModel.moviesNowPlaying,
        viewModel.popularMovies,
        viewModel.topRatedMovies,
        viewModel::handleMovieClick,
        viewModel::handleWatchlistClick,
    )
}

private enum class MovieTab(@param:StringRes val titleRes: Int) {
    Popular(R.string.popular),
    NowPlaying(R.string.now_playing),
    Upcoming(R.string.upcoming),
    TopRated(R.string.top_rated)
}

@Composable
private fun Movies(
    upcomingMovies: Flow<PagingData<MovieUiModel>>,
    moviesNowPlaying: Flow<PagingData<MovieUiModel>>,
    popularMovies: Flow<PagingData<MovieUiModel>>,
    topRatedMovies: Flow<PagingData<MovieUiModel>>,
    onClickMovie: (movieId: Int) -> Unit,
    onClickWatchlist: (MovieUiModel) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { MovieTab.entries.size })
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        PrimaryScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 4.dp
        ) {
            MovieTab.entries.forEachIndexed { index, destination ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        selectedTabIndex = index
                    },
                    text = { TabTitle(destination) }
                )
            }
        }

        HorizontalPager(
            pagerState,
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            key = { page -> page }
        ) { page ->
            when (page) {
                MovieTab.Popular.ordinal -> {
                    Movies(
                        movies = popularMovies.collectAsLazyPagingItems(),
                        onClickMovie = onClickMovie,
                        onWatchlistClick = onClickWatchlist
                    )
                }

                MovieTab.NowPlaying.ordinal -> {
                    Movies(
                        movies = moviesNowPlaying.collectAsLazyPagingItems(),
                        onClickMovie = onClickMovie,
                        onWatchlistClick = onClickWatchlist
                    )
                }

                MovieTab.Upcoming.ordinal -> {
                    Movies(
                        movies = upcomingMovies.collectAsLazyPagingItems(),
                        onClickMovie = onClickMovie,
                        onWatchlistClick = onClickWatchlist
                    )
                }

                MovieTab.TopRated.ordinal -> {
                    Movies(
                        movies = topRatedMovies.collectAsLazyPagingItems(),
                        onClickMovie = onClickMovie,
                        onWatchlistClick = onClickWatchlist
                    )
                }
            }
        }
    }
}

@Composable
private fun TabTitle(tab: MovieTab) {
    Text(
        text = stringResource(tab.titleRes),
        maxLines = 1,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun Movies(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<MovieUiModel>,
    onClickMovie: (movieId: Int) -> Unit,
    onWatchlistClick: (movie: MovieUiModel) -> Unit
) {
    if (movies.loadState.refresh == LoadState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    } else {
        val state = rememberLazyGridState()
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            state = state,
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(movies.itemCount) { index ->
                val movie = movies[index]
                if (movie != null) {
                    InteractivePoster(
                        modifier = Modifier.fillMaxSize(),
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
}

@Preview
@Composable
private fun MoviesScreenPreview() {
    FilmCanTheme {
        val fakeMovies = listOf(
            MovieUiModel(
                id = 1,
                title = "Sample Movie 1",
                imagePath = "/path/to/image1.jpg",
                voteAverage = "8.5",
                inWatchlist = false
            ),
            MovieUiModel(
                id = 2,
                title = "Sample Movie 2",
                imagePath = "/path/to/image2.jpg",
                voteAverage = "7.2",
                inWatchlist = true
            ),
            MovieUiModel(
                id = 3,
                title = "Sample Movie 3",
                imagePath = "/path/to/image3.jpg",
                voteAverage = "9.1",
                inWatchlist = false
            ),
            MovieUiModel(
                id = 4,
                title = "Sample Movie 4",
                imagePath = "/path/to/image4.jpg",
                voteAverage = "6.8",
                inWatchlist = false
            )
        )
        val fakePagingData = flowOf(PagingData.from(fakeMovies))
        Movies(
            upcomingMovies = fakePagingData,
            moviesNowPlaying = fakePagingData,
            popularMovies = fakePagingData,
            topRatedMovies = fakePagingData,
            onClickMovie = {},
            onClickWatchlist = {}
        )
    }
}
