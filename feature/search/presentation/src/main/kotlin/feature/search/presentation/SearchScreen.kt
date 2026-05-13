package feature.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import core.ui.component.ImageQuality
import core.ui.component.ImageType
import core.ui.component.Poster
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object SearchRoute

@Composable
fun SearchScreen(viewModel: SearchViewModel = koinViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResult.collectAsLazyPagingItems()
    val trending = viewModel.trendingThisWeek.collectAsLazyPagingItems()

    Search(
        uiModel,
        searchResults,
        trending,
        viewModel::handleQueryChange,
        viewModel::handleDeleteQueryClick,
        viewModel::handleMovieClick,
        viewModel::handlePersonClick,
        viewModel::handleTvShowClick,
    )
}

@Composable
private fun Search(
    uiModel: SearchUiModel,
    searchResults: LazyPagingItems<ResultUiModel>,
    trending: LazyPagingItems<ResultUiModel>,
    onQueryChange: (query: String) -> Unit,
    onClickDeleteQuery: () -> Unit,
    onClickMovie: (id: Int) -> Unit,
    onClickPerson: (id: Int) -> Unit,
    onClickTvShow: (id: Int) -> Unit,
) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            uiModel.query,
            onQueryChange,
            Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_icon),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            trailingIcon = {
                IconButton(onClick = onClickDeleteQuery) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.delete_icon),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background
            )
        )

        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiModel.showSearchResult) {
                Results(
                    modifier = Modifier.padding(top = 4.dp),
                    results = searchResults,
                    onClickMovie = onClickMovie,
                    onClickPerson = onClickPerson,
                    onClickTvShow = onClickTvShow,
                )
            } else {
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = stringResource(R.string.trending_this_week),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
                Results(
                    modifier = Modifier.padding(top = 4.dp),
                    results = trending,
                    onClickMovie = onClickMovie,
                    onClickPerson = onClickPerson,
                    onClickTvShow = onClickTvShow,
                )
            }
        }
    }
}

@Composable
private fun Results(
    modifier: Modifier = Modifier,
    results: LazyPagingItems<ResultUiModel>,
    onClickMovie: (id: Int) -> Unit,
    onClickPerson: (id: Int) -> Unit,
    onClickTvShow: (id: Int) -> Unit
) {
    if (results.loadState.refresh == LoadState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
    } else {
        val state = rememberLazyGridState()
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            state = state,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(count = results.itemCount) { index ->
                val result = results[index]
                if (result != null) {
                    Poster(
                        modifier = Modifier
                            .clickable {
                                when (result) {
                                    is ResultUiModel.Movie -> onClickMovie(result.id)
                                    is ResultUiModel.Person -> onClickPerson(result.id)
                                    is ResultUiModel.TvShow -> onClickTvShow(result.id)
                                }
                            }
                            .fillMaxSize(),
                        imagePath = result.imagePath,
                        imageType = when (result) {
                            is ResultUiModel.Movie -> ImageType.POSTER
                            is ResultUiModel.Person -> ImageType.PROFILE
                            is ResultUiModel.TvShow -> ImageType.POSTER
                        },
                        imageQuality = ImageQuality.HIGH
                    )
                }
            }
        }

        val keyboardController = LocalSoftwareKeyboardController.current
        LaunchedEffect(Unit) {
            state.interactionSource.interactions.collectLatest {
                if (it is DragInteraction.Start) {
                    keyboardController?.hide()
                }
            }
        }
    }
}
