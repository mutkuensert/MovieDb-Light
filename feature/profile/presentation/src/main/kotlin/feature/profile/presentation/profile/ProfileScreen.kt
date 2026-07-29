package feature.profile.presentation.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import core.ui.FilmCanTheme
import core.ui.LightGreen
import core.ui.TmdbImage
import core.ui.component.FeedLoadError
import core.ui.component.InteractivePoster
import core.ui.component.OneTimeEffect
import core.ui.darkenBy
import feature.profile.presentation.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.milliseconds

@Serializable
object ProfileRoute

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()
    Profile(
        uiModel,
        viewModel.favoriteMovies,
        viewModel.watchlistMovies,
        viewModel.ratedMovies,
        viewModel.favoriteTvShows,
        viewModel.watchlistTvShows,
        viewModel.ratedTvShows,
        viewModel::handleMovieClick,
        viewModel::handleTvShowClick,
        viewModel::logout,
        viewModel::handleSortFavoriteMoviesClick,
        viewModel::handleSortWatchlistMoviesClick,
        viewModel::handleSortRatedMoviesClick,
        viewModel::handleSortFavoriteTvShowsClick,
        viewModel::handleSortWatchlistTvShowsClick,
        viewModel::handleSortRatedTvShowsClick,
        viewModel::handleProfileClick,
    )

    OneTimeEffect { viewModel.initScreen() }
}

@Composable
private fun Profile(
    uiModel: ProfileUiModel,
    favoriteMovies: Flow<PagingData<MovieUiModel>>,
    watchlistMovies: Flow<PagingData<MovieUiModel>>,
    ratedMovies: Flow<PagingData<MovieUiModel>>,
    favoriteTvShows: Flow<PagingData<MovieUiModel>>,
    watchlistTvShows: Flow<PagingData<MovieUiModel>>,
    ratedTvShows: Flow<PagingData<MovieUiModel>>,
    onClickMovie: (movieId: Int) -> Unit,
    onClickTvShow: (tvShowId: Int) -> Unit,
    onLogoutClick: () -> Unit,
    onClickSortFavoriteMoviesBy: (sortBy: SortByUiModel) -> Unit,
    onClickSortWatchlistMoviesBy: (sortBy: SortByUiModel) -> Unit,
    onClickSortRatedMoviesBy: (sortBy: SortByUiModel) -> Unit,
    onClickSortFavoriteTvShowsBy: (sortBy: SortByUiModel) -> Unit,
    onClickSortWatchlistTvShowsBy: (sortBy: SortByUiModel) -> Unit,
    onClickSortRatedTvShowsBy: (sortBy: SortByUiModel) -> Unit,
    onClickProfile: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar(uiModel.profileImagePath, uiModel.name, onClickProfile, onLogoutClick)

        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { ProfileTab.entries.size })
        var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
        LaunchedEffect(pagerState.currentPage) {
            selectedTabIndex = pagerState.currentPage
        }
        Column {
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 4.dp
            ) {
                ProfileTab.entries.forEachIndexed { index, destination ->
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
                Column {
                    when (page) {
                        ProfileTab.WatchlistMovies.ordinal -> {
                            WatchlistMoviesTab(
                                uiModel,
                                onClickSortWatchlistMoviesBy,
                                watchlistMovies.collectAsLazyPagingItems(),
                                onClickMovie
                            )
                        }

                        ProfileTab.FavoriteMovies.ordinal -> {
                            FavoriteMoviesTab(
                                uiModel,
                                onClickSortFavoriteMoviesBy,
                                favoriteMovies.collectAsLazyPagingItems(),
                                onClickMovie
                            )
                        }

                        ProfileTab.RatedMovies.ordinal -> {
                            RatedMoviesTab(
                                uiModel,
                                onClickSortRatedMoviesBy,
                                ratedMovies.collectAsLazyPagingItems(),
                                onClickMovie
                            )
                        }

                        ProfileTab.WatchlistTvShows.ordinal -> {
                            WatchlistTvShowsTab(
                                uiModel,
                                onClickSortWatchlistTvShowsBy,
                                watchlistTvShows.collectAsLazyPagingItems(),
                                onClickTvShow
                            )
                        }

                        ProfileTab.FavoriteTvShows.ordinal -> {
                            FavoriteTvShowsTab(
                                uiModel,
                                onClickSortFavoriteTvShowsBy,
                                favoriteTvShows.collectAsLazyPagingItems(),
                                onClickTvShow
                            )
                        }

                        ProfileTab.RatedTvShows.ordinal -> {
                            RatedTvShowsTab(
                                uiModel,
                                onClickSortRatedTvShowsBy,
                                ratedTvShows.collectAsLazyPagingItems(),
                                onClickTvShow
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RatedTvShowsTab(
    uiModel: ProfileUiModel,
    onClickSortRatedTvShowsBy: (SortByUiModel) -> Unit,
    ratedTvShows: LazyPagingItems<MovieUiModel>,
    onClickTvShow: (Int) -> Unit
) {
    Column {
        SortMenu(
            Modifier.padding(top = 4.dp),
            uiModel.ratedTvShowsSortBy,
            onClickSortRatedTvShowsBy,
        )
        val state = rememberLazyGridState()
        Productions(
            productions = ratedTvShows,
            state = state,
            onClickMovie = onClickTvShow,
        )
        SortByListener(
            ratedTvShows,
            uiModel.ratedTvShowsSortBy,
            state
        )
    }
}

@Composable
private fun FavoriteTvShowsTab(
    uiModel: ProfileUiModel,
    onClickSortFavoriteTvShowsBy: (SortByUiModel) -> Unit,
    favoriteTvShows: LazyPagingItems<MovieUiModel>,
    onClickTvShow: (Int) -> Unit
) {
    Column {
        SortMenu(
            Modifier.padding(top = 4.dp),
            uiModel.favoriteTvShowsSortBy,
            onClickSortFavoriteTvShowsBy,
        )
        val state = rememberLazyGridState()
        Productions(
            productions = favoriteTvShows,
            state = state,
            onClickMovie = onClickTvShow,
        )
        SortByListener(
            favoriteTvShows,
            uiModel.favoriteTvShowsSortBy,
            state
        )
    }
}

@Composable
private fun WatchlistTvShowsTab(
    uiModel: ProfileUiModel,
    onClickSortWatchlistTvShowsBy: (SortByUiModel) -> Unit,
    watchlistTvShows: LazyPagingItems<MovieUiModel>,
    onClickTvShow: (Int) -> Unit
) {
    Column {
        SortMenu(
            Modifier.padding(top = 4.dp),
            uiModel.watchlistTvShowsSortBy,
            onClickSortWatchlistTvShowsBy,
        )
        val state = rememberLazyGridState()
        Productions(
            productions = watchlistTvShows,
            state = state,
            onClickMovie = onClickTvShow,
        )
        SortByListener(
            watchlistTvShows,
            uiModel.watchlistTvShowsSortBy,
            state
        )
    }
}

@Composable
private fun RatedMoviesTab(
    uiModel: ProfileUiModel,
    onClickSortRatedMoviesBy: (SortByUiModel) -> Unit,
    ratedMovies: LazyPagingItems<MovieUiModel>,
    onClickMovie: (Int) -> Unit
) {
    Column {
        SortMenu(
            Modifier.padding(top = 4.dp),
            uiModel.ratedMoviesSortBy,
            onClickSortRatedMoviesBy,
        )
        val state = rememberLazyGridState()
        Productions(
            productions = ratedMovies,
            state = state,
            onClickMovie = onClickMovie,
        )
        SortByListener(
            ratedMovies,
            uiModel.ratedMoviesSortBy,
            state
        )
    }
}

@Composable
private fun FavoriteMoviesTab(
    uiModel: ProfileUiModel,
    onClickSortFavoriteMoviesBy: (SortByUiModel) -> Unit,
    favoriteMovies: LazyPagingItems<MovieUiModel>,
    onClickMovie: (Int) -> Unit
) {
    Column {
        SortMenu(
            Modifier.padding(top = 4.dp),
            uiModel.favoriteMoviesSortBy,
            onClickSortFavoriteMoviesBy,
        )
        val state = rememberLazyGridState()
        Productions(
            productions = favoriteMovies,
            state = state,
            onClickMovie = onClickMovie,
        )
        SortByListener(
            favoriteMovies,
            uiModel.favoriteMoviesSortBy,
            state
        )
    }
}

@Composable
private fun WatchlistMoviesTab(
    uiModel: ProfileUiModel,
    onClickSortWatchlistMoviesBy: (SortByUiModel) -> Unit,
    watchlistMovies: LazyPagingItems<MovieUiModel>,
    onClickMovie: (Int) -> Unit
) {
    Column {
        SortMenu(
            Modifier.padding(top = 4.dp),
            uiModel.watchlistMoviesSortBy,
            onClickSortWatchlistMoviesBy,
        )
        val state = rememberLazyGridState()
        Productions(
            productions = watchlistMovies,
            state = state,
            onClickMovie = onClickMovie,
        )
        SortByListener(
            watchlistMovies,
            uiModel.watchlistMoviesSortBy,
            state
        )
    }
}

private enum class ProfileTab(@param:StringRes val titleRes: Int) {
    WatchlistMovies(R.string.watchlist_movies),
    FavoriteMovies(R.string.favorite_movies),
    RatedMovies(R.string.rated_movies),
    WatchlistTvShows(R.string.watchlist_tv_shows),
    FavoriteTvShows(R.string.favorite_tv_shows),
    RatedTvShows(R.string.rated_tv_shows),
}

@Composable
private fun TabTitle(tab: ProfileTab) {
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
private fun Productions(
    modifier: Modifier = Modifier,
    productions: LazyPagingItems<MovieUiModel>,
    state: LazyGridState,
    onClickMovie: (movieId: Int) -> Unit,
) {
    if (productions.loadState.refresh == LoadState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    } else if (productions.loadState.append is LoadState.Error || productions.loadState.refresh is LoadState.Error) {
        FeedLoadError(onRetryClick = productions::retry)
    } else {
        PullToRefreshBox(
            isRefreshing = productions.loadState.refresh is LoadState.Loading,
            onRefresh = productions::refresh,
            modifier
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                state = state,
                contentPadding = PaddingValues(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(
                    count = productions.itemCount,
                    key = { index ->
                        productions[index]?.id ?: index
                    }) { index ->
                    val movie = productions[index]
                    if (movie != null) {
                        InteractivePoster(
                            modifier = Modifier.fillMaxSize(),
                            imagePath = movie.imagePath,
                            title = movie.title,
                            vote = movie.voteAverage,
                            onPosterClick = { onClickMovie(movie.id) },
                        )
                    }
                }

                if (productions.loadState.append is LoadState.Loading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) { CircularProgressIndicator() }
                    }
                }
            }
        }
    }
}

//https://issuetracker.google.com/issues/209652366?hl=ru
@Composable
private fun SortByListener(
    movies: LazyPagingItems<MovieUiModel>,
    sortBy: SortByUiModel,
    gridState: LazyGridState
) {
    var previousSortBy: SortByUiModel by rememberSaveable { mutableStateOf(sortBy) }
    LaunchedEffect(movies.itemSnapshotList) {
        if (previousSortBy != sortBy) {
            delay(500.milliseconds) //To fix race condition between internal scroll based on item key and this scroll
            gridState.requestScrollToItem(0)
            previousSortBy = sortBy
        }
    }
}

@Composable
private fun SortMenu(
    modifier: Modifier = Modifier,
    sortBy: SortByUiModel,
    onClickSortBy: (sortBy: SortByUiModel) -> Unit
) {
    Box(modifier) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier)
            var expanded by remember { mutableStateOf(false) }
            Box {
                IconButton({ expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = stringResource(R.string.logout_button_icon),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = MaterialTheme.colorScheme.surface.darkenBy(30)
                ) {
                    Column(Modifier.padding(horizontal = 4.dp)) {
                        Text(stringResource(R.string.sort_by_added_time))

                        DropdownMenuItem(
                            text = {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(stringResource(R.string.ascending))
                                    Icon(
                                        Icons.Filled.CheckCircle,
                                        stringResource(R.string.sort_by_ascending_selected_icon),
                                        tint = if (sortBy == SortByUiModel.ASCENDING) {
                                            LightGreen
                                        } else {
                                            Color.White
                                        }
                                    )
                                }
                            },
                            onClick = {
                                expanded = false
                                onClickSortBy(SortByUiModel.ASCENDING)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(stringResource(R.string.descending))
                                    Icon(
                                        Icons.Filled.CheckCircle,
                                        stringResource(R.string.sort_by_descending_selected_icon),
                                        tint = if (sortBy == SortByUiModel.DESCENDING) {
                                            LightGreen
                                        } else {
                                            Color.White
                                        }
                                    )
                                }
                            },
                            onClick = {
                                expanded = false
                                onClickSortBy(SortByUiModel.DESCENDING)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SortMenuPreview() {
    FilmCanTheme {
        SortMenu(sortBy = SortByUiModel.ASCENDING) { }
    }
}

@Composable
private fun TopBar(
    imagePath: String?,
    text: String,
    onClickProfile: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier.clickable { onClickProfile() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(Color.White)
            ) {
                if (imagePath != null) {
                    AsyncImage(
                        TmdbImage(imagePath).originalSizedUrl,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(onClick = onLogoutClick, Modifier.padding(end = 16.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = stringResource(R.string.logout_button_icon),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
private fun ProfilePreview() {
    val emptyLazyPagingItems =
        flowOf(PagingData.empty<MovieUiModel>())
    FilmCanTheme {
        Profile(
            ProfileUiModel(
                null,
                "Your name",
                SortByUiModel.ASCENDING,
                SortByUiModel.ASCENDING,
                SortByUiModel.ASCENDING,
                SortByUiModel.ASCENDING,
                SortByUiModel.ASCENDING,
                SortByUiModel.ASCENDING,
            ),
            emptyLazyPagingItems,
            emptyLazyPagingItems,
            emptyLazyPagingItems,
            emptyLazyPagingItems,
            emptyLazyPagingItems,
            emptyLazyPagingItems,
            {},
            {},
            {}, {},
            {},
            {},
            {},
            {},
            {},
            {})
    }
}
