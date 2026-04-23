package feature.tvshow.presentation.list

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
import core.ui.MoviedbLightTheme
import core.ui.StatusBarColorHandler
import core.ui.component.InteractivePoster
import feature.tvshow.presentation.R
import feature.tvshow.presentation.list.model.TvShowUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun TvShowsScreen(
    viewModel: TvShowsViewModel = koinViewModel()
) {
    TvShows(
        viewModel.upcomingTvShows,
        viewModel.tvShowsAiringToday,
        viewModel.popularTvShows,
        viewModel.topRatedTvShows,
        viewModel::handleTvShowClick,
        viewModel::handleWatchlistClick,
    )

    StatusBarColorHandler(MaterialTheme.colorScheme.background)
}

private enum class TvShowTab(@param:StringRes val titleRes: Int) {
    Popular(R.string.popular),
    NowPlaying(R.string.airing_today),
    Upcoming(R.string.upcoming),
    TopRated(R.string.top_rated)
}

@Composable
private fun TvShows(
    upcomingTvShows: Flow<PagingData<TvShowUiModel>>,
    tvShowsAiringToday: Flow<PagingData<TvShowUiModel>>,
    popularTvShows: Flow<PagingData<TvShowUiModel>>,
    topRatedTvShows: Flow<PagingData<TvShowUiModel>>,
    onClickTvShow: (tvShowId: Int) -> Unit,
    onClickWatchlist: (TvShowUiModel) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = {
        4
    })
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
            TvShowTab.entries.forEachIndexed { index, destination ->
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
                TvShowTab.Popular.ordinal -> {
                    TvShows(
                        tvShows = popularTvShows.collectAsLazyPagingItems(),
                        onClickTvShow = onClickTvShow,
                        onWatchlistClick = onClickWatchlist
                    )
                }

                TvShowTab.NowPlaying.ordinal -> {
                    TvShows(
                        tvShows = tvShowsAiringToday.collectAsLazyPagingItems(),
                        onClickTvShow = onClickTvShow,
                        onWatchlistClick = onClickWatchlist
                    )
                }

                TvShowTab.Upcoming.ordinal -> {
                    TvShows(
                        tvShows = upcomingTvShows.collectAsLazyPagingItems(),
                        onClickTvShow = onClickTvShow,
                        onWatchlistClick = onClickWatchlist
                    )
                }

                TvShowTab.TopRated.ordinal -> {
                    TvShows(
                        tvShows = topRatedTvShows.collectAsLazyPagingItems(),
                        onClickTvShow = onClickTvShow,
                        onWatchlistClick = onClickWatchlist
                    )
                }
            }
        }
    }
}

@Composable
private fun TabTitle(tab: TvShowTab) {
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
private fun TvShows(
    modifier: Modifier = Modifier,
    tvShows: LazyPagingItems<TvShowUiModel>,
    onClickTvShow: (tvShowId: Int) -> Unit,
    onWatchlistClick: (tvShow: TvShowUiModel) -> Unit
) {
    if (tvShows.loadState.refresh == LoadState.Loading) {
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
            items(
                count = tvShows.itemCount,
                key = { index ->
                    tvShows[index]?.id ?: Random(seed = index).nextInt()
                }) { index ->
                val tvShow = tvShows[index]
                if (tvShow != null) {
                    InteractivePoster(
                        modifier = Modifier.fillMaxSize(),
                        imagePath = tvShow.imagePath,
                        title = tvShow.title,
                        vote = tvShow.voteAverage,
                        onPosterClick = { onClickTvShow(tvShow.id) },
                        inWatchlist = tvShow.inWatchlist,
                        onWatchlistClick = { onWatchlistClick(tvShow) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TvShowsScreenPreview() {
    MoviedbLightTheme {
        val fakeTvShows = listOf(
            TvShowUiModel(
                id = 1,
                title = "Sample TV show 1",
                imagePath = "/path/to/image1.jpg",
                voteAverage = "8.5",
                inWatchlist = false
            ),
            TvShowUiModel(
                id = 2,
                title = "Sample TV show 2",
                imagePath = "/path/to/image2.jpg",
                voteAverage = "7.2",
                inWatchlist = true
            ),
            TvShowUiModel(
                id = 3,
                title = "Sample TV show 3",
                imagePath = "/path/to/image3.jpg",
                voteAverage = "9.1",
                inWatchlist = false
            ),
            TvShowUiModel(
                id = 4,
                title = "Sample TV show 4",
                imagePath = "/path/to/image4.jpg",
                voteAverage = "6.8",
                inWatchlist = false
            )
        )
        val fakePagingData = flowOf(PagingData.from(fakeTvShows))
        TvShows(
            upcomingTvShows = fakePagingData,
            tvShowsAiringToday = fakePagingData,
            popularTvShows = fakePagingData,
            topRatedTvShows = fakePagingData,
            onClickTvShow = {},
            onClickWatchlist = {}
        )
    }
}
