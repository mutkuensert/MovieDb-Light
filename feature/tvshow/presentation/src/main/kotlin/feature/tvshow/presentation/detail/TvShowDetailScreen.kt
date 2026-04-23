package feature.tvshow.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Beenhere
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import core.ui.AppColors
import core.ui.MoviedbLightTheme
import core.ui.TmdbImage
import core.ui.coil.debugPlaceholder
import core.ui.component.ImageType
import core.ui.component.InteractivePoster
import core.ui.component.OneTimeEffect
import core.ui.component.Poster
import core.ui.component.PosterHeight
import core.ui.component.YoutubeVideoPlayer
import core.ui.darkenBy
import feature.tvshow.presentation.R
import feature.tvshow.presentation.detail.model.PersonUiModel
import feature.tvshow.presentation.detail.model.TvShowDetailUiModel
import feature.tvshow.presentation.detail.model.TvShowUiModel
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun TvShowDetailScreen(viewModel: TvShowDetailViewModel = koinViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()
    val similarTvShows = viewModel.similarTvShows.collectAsLazyPagingItems()

    TvShowDetail(
        uiModel,
        viewModel::handleStreamServicesInfoButton,
        similarTvShows,
        viewModel::handleTvShowClick,
        viewModel::handlePersonClick,
        viewModel::handleWatchlistClick,
        viewModel::handleFavoriteClick,
        viewModel::handleRateClick,
        viewModel::handleRemoveRatingClick,
    )

    OneTimeEffect {
        viewModel.getDetails()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TvShowDetail(
    uiModel: TvShowDetailUiModel,
    onClickStreamingServicesInfoButton: () -> Unit,
    similarTvShows: LazyPagingItems<TvShowUiModel>,
    onClickTvShow: (id: Int) -> Unit,
    onClickPerson: (id: Int) -> Unit,
    onClickWatchlist: (id: Int, inWatchlist: Boolean) -> Unit,
    onClickFavorite: () -> Unit,
    onRateClick: (value: Int) -> Unit,
    onRemoveRatingClick: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            ActionButtons(
                uiModel,
                onClickFavorite,
                onClickWatchlist,
                onRateClick,
                onRemoveRatingClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
        ) {
            TvShowPosters(uiModel.imagePaths, uiModel.year, uiModel.vote, uiModel.runtime)

            Column(Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Providers(
                        uiModel.providerLogoPaths,
                        onClickStreamingServicesInfoButton,
                        Modifier.padding(top = 4.dp)
                    )
                }

                if (uiModel.genres.isNotBlank()) {
                    Text(
                        text = uiModel.genres,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground.darkenBy(30),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (uiModel.overview.isNotBlank()) {
                    Overview(uiModel.overview, Modifier.padding(top = 8.dp))
                }

                if (uiModel.trailerYoutubeVideoId != null) {
                    YoutubeVideoPlayer(
                        uiModel.trailerYoutubeVideoId,
                        Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Cast(uiModel, onClickPerson)

            SimilarTvShows(similarTvShows, onClickTvShow, onClickWatchlist)
        }
    }
}

@Composable
private fun ActionButtons(
    uiModel: TvShowDetailUiModel,
    onClickFavorite: () -> Unit,
    onClickWatchlist: (Int, Boolean) -> Unit,
    onRateClick: (Int) -> Unit,
    onRemoveRatingClick: () -> Unit
) {
    var extended by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (extended) {
                if (uiModel.showFavoriteButton && uiModel.favorite != null) {
                    FavoriteButton(
                        uiModel.favorite,
                        onClickFavorite
                    )
                }

                if (uiModel.showWatchlistButton && uiModel.inWatchlist != null) {
                    WatchlistButton(
                        uiModel.inWatchlist,
                        {
                            onClickWatchlist(uiModel.id, !uiModel.inWatchlist)
                        }
                    )
                }

                if (uiModel.showRateButton) {
                    RateButton(
                        uiModel.userRate,
                        onRateClick,
                        onRemoveRatingClick
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        if (uiModel.showRateButton || uiModel.showFavoriteButton || uiModel.showWatchlistButton) {
            FloatingActionButton(
                onClick = { extended = !extended },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    if (extended) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                    "Small floating action button."
                )
            }
        }
    }
}

@Composable
private fun TvShowPosters(
    imagePaths: List<String>,
    year: String,
    vote: String,
    runtime: String,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = {
        imagePaths.size
    })
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var firstImageHeight: Dp? by remember { mutableStateOf(null) }
    val density = LocalDensity.current

    Box(modifier) {
        var success by remember { mutableStateOf(false) }
        FirstPageHintEffect(pagerState, success)

        HorizontalPager(
            pagerState,
            Modifier
                .fillMaxWidth()
                .then(
                    if (firstImageHeight != null) {
                        Modifier.height(firstImageHeight!!)
                    } else {
                        Modifier
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            key = { page -> page }
        ) { page ->
            Box {
                var loading by remember { mutableStateOf(false) }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(TmdbImage(imagePaths[page]).originalSizedUrl)
                        .crossfade(true)
                        .allowHardware(true)
                        .build(),
                    onLoading = { loading = true },
                    onSuccess = {
                        loading = false
                        success = true
                    },
                    onError = {
                        loading = false
                        success = false
                    },
                    error = debugPlaceholder(core.ui.R.drawable.debug_placeholder_dog),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            if (success && firstImageHeight == null) {
                                firstImageHeight = with(density) { it.size.height.toDp() }
                            }
                        },
                    contentDescription = stringResource(core.ui.R.string.image)
                )

                if (loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenWidth * 3f / 2f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                    }
                }

                if (page == 0) {
                    BottomGradient(Modifier.align(Alignment.BottomCenter))

                    YearVoteRuntimeText(
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 2.dp),
                        year,
                        vote,
                        runtime
                    )
                }
            }
        }

        if (pagerState.currentPage != 0 && imagePaths.size > 2) {
            PageIndicator(pagerState)
        }
    }
}

@Composable
fun BoxScope.PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.White,
    indicatorSize: Dp = 16.dp,
    spacing: Dp = 6.dp,
    visibleDotsCount: Int = 5
) {
    val totalItemWidth = indicatorSize + spacing
    val boxWidth = (visibleDotsCount * (indicatorSize + spacing)) - spacing
    var targetOffset by remember { mutableStateOf(0.dp) }
    val offset = animateDpAsState(targetOffset)

    LaunchedEffect(pagerState.currentPage) {
        val currentPage = pagerState.currentPage
        if (currentPage < visibleDotsCount - 2) {
            targetOffset = 0.dp
            return@LaunchedEffect
        }
        var offsetDiff = if (pagerState.lastScrolledForward) {
            totalItemWidth * -1
        } else {
            totalItemWidth
        }

        if (currentPage == pagerState.pageCount - 1) {
            offsetDiff -= spacing
        }
        targetOffset += offsetDiff
    }

    Box(
        modifier = modifier
            .width(boxWidth)
            .height(indicatorSize)
            .clipToBounds()
            .align(Alignment.BottomCenter),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.Start, unbounded = true)
                .offset(x = offset.value),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) activeColor else inactiveColor

                Box(
                    modifier = Modifier
                        .size(indicatorSize)
                        .graphicsLayer {
                            val currentPage = pagerState.currentPage
                            if (currentPage != iteration) {
                                scaleX = 0.5f
                                scaleY = 0.5f
                            }
                        }
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@Composable
private fun BottomGradient(modifier: Modifier = Modifier) {
    Box(modifier) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xCB000000)
                        ),
                    )
                )
        )
    }
}

@Composable
private fun FirstPageHintEffect(pagerState: PagerState, showHint: Boolean) {
    var hasHinted by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    LaunchedEffect(showHint) {
        if (!hasHinted && pagerState.pageCount > 1 && showHint) {
            hasHinted = true

            repeat(2) {
                pagerState.animateScrollBy(
                    value = with(density) { 30.dp.toPx() },
                    animationSpec = tween()
                )

                pagerState.animateScrollBy(
                    value = -with(density) { 30.dp.toPx() },
                    animationSpec = tween()
                )
            }
        }
    }
}

@Composable
private fun Overview(overview: String, modifier: Modifier = Modifier) {
    var collapsed by remember { mutableStateOf(true) }

    Box(modifier.clickable {
        collapsed = !collapsed
    }) {
        Text(
            modifier = Modifier
                .then(if (collapsed) Modifier.height(92.dp) else Modifier),
            text = overview,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        if (collapsed) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .align(Alignment.BottomEnd)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            ),
                        )
                    )
            )
        }
    }
}

@Composable
private fun Providers(
    providerLogoPaths: List<String>,
    onClickStreamingServicesInfoButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            providerLogoPaths.forEachIndexed { index, logoPath ->
                AsyncImage(
                    model = TmdbImage(logoPath).originalSizedUrl,
                    error = debugPlaceholder(libraries.R.drawable.tmdb_logo_blue_square),
                    modifier = Modifier
                        .height(36.dp)
                        .clip(MaterialTheme.shapes.extraSmall),
                    contentDescription = stringResource(core.ui.R.string.image)
                )
                if (index != providerLogoPaths.lastIndex) {
                    Spacer(Modifier.width(4.dp))
                }
            }
        }
        if (providerLogoPaths.isNotEmpty()) {
            IconButton(onClickStreamingServicesInfoButton, Modifier.padding(start = 2.dp)) {
                Icon(
                    modifier = Modifier.height(24.dp),
                    imageVector = Icons.Filled.Info,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = stringResource(R.string.streaming_services_info_button_icon)
                )
            }
        }
    }
}

@Composable
private fun Cast(
    uiModel: TvShowDetailUiModel,
    onClickPerson: (personId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier.height(260.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(uiModel.cast.size) { index ->
            val person = uiModel.cast[index]
            Person(
                person.id,
                person.name,
                person.character,
                person.imagePath,
                onClickPerson,
                Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
private fun SimilarTvShows(
    similarTvShows: LazyPagingItems<TvShowUiModel>,
    onClickTvShow: (tvShowId: Int) -> Unit,
    onClickWatchlist: (id: Int, inWatchlist: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        if (similarTvShows.itemCount != 0) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                text = stringResource(R.string.similar),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }

        LazyRow(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                if (similarTvShows.loadState.refresh == LoadState.Loading) {
                    Box(
                        modifier = Modifier
                            .height(PosterHeight.large)
                            .fillParentMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
            }

            items(
                count = similarTvShows.itemCount,
                key = similarTvShows.itemKey { it.id }
            ) { index ->
                val tvShow = similarTvShows[index]

                if (tvShow != null) {
                    InteractivePoster(
                        modifier = Modifier
                            .height(PosterHeight.large)
                            .padding(horizontal = 6.dp, vertical = 10.dp)
                            .then(
                                when (index) {
                                    0 -> Modifier.padding(start = 10.dp)
                                    similarTvShows.itemCount - 1 if similarTvShows.loadState != LoadState.Loading -> {
                                        Modifier.padding(end = 10.dp)
                                    }

                                    else -> Modifier
                                }
                            ),
                        imagePath = tvShow.imagePath,
                        title = tvShow.title,
                        vote = tvShow.voteAverage,
                        onPosterClick = { onClickTvShow(tvShow.id) },
                        inWatchlist = tvShow.inWatchlist,
                        onWatchlistClick = { onClickWatchlist(tvShow.id, !tvShow.inWatchlist!!) }
                    )
                }
            }
        }
    }
}

@Composable
private fun YearVoteRuntimeText(
    modifier: Modifier = Modifier,
    year: String,
    vote: String,
    runtime: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = year,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )

        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Filled.Star,
            tint = AppColors.star,
            contentDescription = stringResource(core.ui.R.string.vote_icon)
        )

        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = vote,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )

        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = stringResource(R.string.runtime, runtime),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun Person(
    id: Int,
    name: String,
    character: String,
    imagePath: String?,
    onClickPerson: (personId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClickPerson(id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Poster(
            Modifier.height(PosterHeight.medium),
            imagePath,
            ImageType.PROFILE
        )

        Spacer(Modifier.height(4.dp))

        Box(modifier = Modifier.width(120.dp), contentAlignment = Alignment.TopCenter) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(4.dp))

        Box(modifier = Modifier.width(120.dp), contentAlignment = Alignment.TopCenter) {
            Text(
                text = character,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchlistButton(
    inWatchlist: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (inWatchlist) {
        Color(0xFFFFEB3B)
    } else {
        Color(0xFFE1E1E1)
    }
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = {
            PlainTooltip { Text(stringResource(R.string.add_to_or_remove_from_watchlist_button)) }
        },
        state = rememberTooltipState()
    ) {
        FloatingActionButton(
            onClick,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Default.Beenhere,
                tint = color,
                contentDescription = stringResource(core.ui.R.string.watchlist_button)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteButton(
    favorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (favorite) {
        Color(0xFFD00000)
    } else {
        Color(0xFFE1E1E1)
    }
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = {
            PlainTooltip { Text(stringResource(R.string.add_to_or_remove_from_favorites_button)) }
        },
        state = rememberTooltipState()
    ) {
        FloatingActionButton(
            onClick,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Default.Favorite,
                tint = color,
                contentDescription = stringResource(R.string.add_to_or_remove_from_favorites_button)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RateButton(
    rate: String?,
    onRateClick: (rate: Int) -> Unit,
    onRemoveRatingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRateBottomSheetVisible by remember { mutableStateOf(false) }
    val color = if (rate != null) {
        Color(0xFFFFEB3B)
    } else {
        Color(0xFFE1E1E1)
    }
    Row(
        modifier
            .clickable { isRateBottomSheetVisible = true }
            .background(
                MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
                MaterialTheme.shapes.medium
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(20.dp),
            imageVector = if (rate != null) Icons.Filled.Star else Icons.Filled.StarBorder,
            tint = color,
            contentDescription = stringResource(R.string.rate_button)
        )

        Text(stringResource(R.string.rate, rate ?: ""))
    }

    if (isRateBottomSheetVisible) {
        RateBottomSheet(
            rate?.toIntOrNull(),
            { isRateBottomSheetVisible = false },
            onRateClick,
            onRemoveRatingClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateBottomSheet(
    rate: Int?,
    onDismiss: () -> Unit,
    onClickRate: (rate: Int) -> Unit,
    onRemoveRate: () -> Unit
) {
    ModalBottomSheet(
        onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.rate_the_tv_show),
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                (1..10).forEach { value ->
                    Column(
                        modifier = Modifier.padding(start = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    onClickRate(value)
                                    onDismiss()
                                },
                            imageVector = if (rate != null && value <= rate) Icons.Filled.Star else Icons.Filled.StarBorder,
                            tint = if (rate != null && value <= rate) {
                                AppColors.star
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            },
                            contentDescription = stringResource(R.string.rate_button)
                        )
                        Text(text = value.toString())
                    }
                }
            }

            TextButton({
                onRemoveRate()
                onDismiss()
            }, Modifier.padding(vertical = 16.dp)) {
                Text(
                    stringResource(R.string.remove_your_rating),
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun TvShowDetailPreview() {
    MoviedbLightTheme {
        val emptyPagingData =
            flowOf(PagingData.from<TvShowUiModel>(emptyList())).collectAsLazyPagingItems()

        val cast = listOf(
            PersonUiModel(
                id = 2723,
                imagePath = null,
                name = "Some Person",
                character = "recteque"
            ),
            PersonUiModel(
                id = 2724,
                imagePath = null,
                name = "Some Person",
                character = "recteque"
            ),
            PersonUiModel(
                id = 2725,
                imagePath = null,
                name = "Some Person",
                character = "recteque"
            ),
            PersonUiModel(
                id = 2726,
                imagePath = null,
                name = "Some Person",
                character = "recteque"
            ),
            PersonUiModel(
                id = 2727,
                imagePath = null,
                name = "Some Person",
                character = "recteque"
            ),
        )

        TvShowDetail(
            TvShowDetailUiModel(
                id = -1,
                imagePaths = listOf("", "", "", "", "", "", "", ""),
                title = "pharetra",
                vote = "7.1",
                showFavoriteButton = true,
                showWatchlistButton = true,
                showRateButton = true,
                userRate = "4",
                inWatchlist = true,
                favorite = true,
                runtime = "120",
                year = "2010",
                genres = "Comedy, Action",
                overview = LoremIpsum(20).values.joinToString(" "),
                providerLogoPaths = listOf("path", "path2"),
                trailerYoutubeVideoId = "123",
                cast = cast,
            ),
            {},
            emptyPagingData,
            {},
            {},
            { _, _ -> },
            {},
            {},
            {}
        )
    }
}
