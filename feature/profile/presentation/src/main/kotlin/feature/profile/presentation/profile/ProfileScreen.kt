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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import core.ui.LightGreen
import core.ui.MoviedbLightTheme
import core.ui.StatusBarColorHandler
import core.ui.TmdbImage
import core.ui.component.InteractivePoster
import core.ui.darkenBy
import feature.profile.presentation.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object ProfileRoute

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()
    val favoriteMovies = viewModel.favoriteMovies.collectAsLazyPagingItems()
    val watchlistMovies = viewModel.watchlistMovies.collectAsLazyPagingItems()
    val ratedMovies = viewModel.ratedMovies.collectAsLazyPagingItems()

    Profile(
        uiModel,
        favoriteMovies,
        watchlistMovies,
        ratedMovies,
        viewModel::handleMovieClick,
        viewModel::logout,
        viewModel::handleSortFavoriteMoviesClick,
        viewModel::handleSortWatchlistMoviesClick,
        viewModel::handleSortRatedMoviesClick,
        viewModel::handleProfilePictureClick,
    )

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.initScreen() }
    StatusBarColorHandler(MaterialTheme.colorScheme.background)
}

@Composable
private fun Profile(
    uiModel: ProfileUiModel,
    favoriteMovies: LazyPagingItems<MovieUiModel>,
    watchlistMovies: LazyPagingItems<MovieUiModel>,
    ratedMovies: LazyPagingItems<MovieUiModel>,
    onClickMovie: (movieId: Int) -> Unit,
    onLogoutClick: () -> Unit,
    onClickSortFavoriteMoviesBy: (sortBy: SortByUiModel) -> Unit,
    onClickSortWatchlistMoviesBy: (sortBy: SortByUiModel) -> Unit,
    onClickSortRatedMoviesBy: (sortBy: SortByUiModel) -> Unit,
    onClickProfilePicture: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar(uiModel.profileImagePath, uiModel.name, onClickProfilePicture, onLogoutClick)

        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { 3 })
        var selectedTabIndex by remember { mutableIntStateOf(0) }
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
                        0 -> {
                            WatchlistMoviesTab(
                                uiModel,
                                onClickSortWatchlistMoviesBy,
                                watchlistMovies,
                                onClickMovie
                            )
                        }

                        1 -> {
                            FavoriteMoviesTab(
                                uiModel,
                                onClickSortFavoriteMoviesBy,
                                favoriteMovies,
                                onClickMovie
                            )
                        }

                        2 -> {
                            RatedMoviesTab(
                                uiModel,
                                onClickSortRatedMoviesBy,
                                ratedMovies,
                                onClickMovie
                            )
                        }
                    }
                }
            }
        }
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
        Filter(
            Modifier.padding(top = 4.dp),
            uiModel.ratedMoviesSortBy,
            onClickSortRatedMoviesBy,
        )
        val state = rememberLazyGridState()
        Movies(
            movies = ratedMovies,
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
        Filter(
            Modifier.padding(top = 4.dp),
            uiModel.favoriteMoviesSortBy,
            onClickSortFavoriteMoviesBy,
        )
        val state = rememberLazyGridState()
        Movies(
            movies = favoriteMovies,
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
        Filter(
            Modifier.padding(top = 4.dp),
            uiModel.watchlistMoviesSortBy,
            onClickSortWatchlistMoviesBy,
        )
        val state = rememberLazyGridState()
        Movies(
            movies = watchlistMovies,
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
private fun Movies(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<MovieUiModel>,
    state: LazyGridState,
    onClickMovie: (movieId: Int) -> Unit,
) {
    if (movies.loadState.refresh == LoadState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    } else {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            state = state,
            contentPadding = PaddingValues(vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(
                count = movies.itemCount,
                key = { index ->
                    movies[index]?.id ?: index
                }) { index ->
                val movie = movies[index]
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
    var previousSortBy: SortByUiModel? by rememberSaveable { mutableStateOf(null) }
    LaunchedEffect(movies.itemSnapshotList) {
        if (previousSortBy != sortBy) {
            delay(500) //To fix race condition between internal scroll based on item key and this scroll
            gridState.requestScrollToItem(0)
            previousSortBy = sortBy
        }
    }
}

@Composable
private fun Filter(
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
private fun FilterPreview() {
    MoviedbLightTheme {
        Filter(sortBy = SortByUiModel.ASCENDING) { }
    }
}

@Composable
private fun TopBar(
    imagePath: String?,
    text: String,
    onClickProfilePicture: () -> Unit,
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
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .clip(CircleShape)
                .size(40.dp)
                .background(Color.White)
                .clickable { onClickProfilePicture() }
        ) {
            if (imagePath != null) {
                AsyncImage(
                    TmdbImage(imagePath).originalSizedUrl,
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
        flowOf(PagingData.empty<MovieUiModel>()).collectAsLazyPagingItems()
    MoviedbLightTheme {
        Profile(
            ProfileUiModel(
                null,
                "Your name",
                SortByUiModel.ASCENDING,
                SortByUiModel.ASCENDING,
                SortByUiModel.ASCENDING,
            ),
            emptyLazyPagingItems,
            emptyLazyPagingItems,
            emptyLazyPagingItems,
            {},
            {}, {},
            {},
            {},
            {})
    }
}
