package feature.profile.presentation.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import core.ui.MoviedbLightTheme
import core.ui.StatusBarColorHandler
import core.ui.component.InteractivePoster
import core.ui.component.PosterSize
import feature.profile.presentation.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
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
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBar(uiModel.profileImageUrl, uiModel.name, onClickProfilePicture, onLogoutClick)

        FeedHeader(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
            stringResource(R.string.favorite_movies),
            uiModel.favoriteMoviesSortBy,
            onClickSortFavoriteMoviesBy,
        )

        Movies(favoriteMovies, uiModel.favoriteMoviesSortBy, onClickMovie)

        FeedHeader(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
            stringResource(R.string.watchlist_movies),
            uiModel.watchlistMoviesSortBy,
            onClickSortWatchlistMoviesBy,
        )

        Movies(watchlistMovies, uiModel.watchlistMoviesSortBy, onClickMovie)

        FeedHeader(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
            stringResource(R.string.rated_movies),
            uiModel.ratedMoviesSortBy,
            onClickSortRatedMoviesBy,
        )

        Movies(ratedMovies, uiModel.ratedMoviesSortBy, onClickMovie)
    }
}

//https://issuetracker.google.com/issues/209652366?hl=ru
@Composable
private fun ScrollFeedToStartWhenSortedAgain(
    movies: LazyPagingItems<MovieUiModel>,
    sortBy: SortByUiModel,
    listState: LazyListState
) {
    var previousSortBy: SortByUiModel? by rememberSaveable { mutableStateOf(null) }
    LaunchedEffect(movies.itemSnapshotList) {
        if (previousSortBy != sortBy) {
            delay(500) //To fix race condition between internal scroll based on item key and this scroll
            listState.requestScrollToItem(0)
            previousSortBy = sortBy
        }
    }
}

@Composable
private fun FeedHeader(
    modifier: Modifier = Modifier,
    text: String,
    sortBy: SortByUiModel,
    onClickSortBy: (sortBy: SortByUiModel) -> Unit
) {
    Column(modifier) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

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
                    containerColor = MaterialTheme.colorScheme.surface
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
                                            Color(0xFF30F100)
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
                                            Color(0xFF30F100)
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
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun TopBar(
    imageUrl: String?,
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
            if (imageUrl != null) {
                AsyncImage(
                    imageUrl,
                    contentDescription = null
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Text(text = text, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)

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

@Composable
private fun Movies(
    movies: LazyPagingItems<MovieUiModel>,
    sortBy: SortByUiModel,
    onClickMovie: (movieId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = lazyListState,
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
                )
            }
        }
    }

    ScrollFeedToStartWhenSortedAgain(
        movies,
        sortBy,
        lazyListState
    )
}

@Preview
@Composable
private fun ProfilePreview() {
    val emptyLazyPagingItems = flowOf(PagingData.empty<MovieUiModel>()).collectAsLazyPagingItems()
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
