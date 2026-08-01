package feature.tvshow.presentation.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import core.ui.AppColors
import core.ui.component.FeedLoadError
import core.ui.darkenBy
import feature.tvshow.presentation.detail.model.ReviewUiModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun TvShowReviewsScreen(viewModel: TvShowReviewsViewModel = hiltViewModel()) {
    ReviewsFeed(
        reviews = viewModel.reviews.collectAsLazyPagingItems(),
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ReviewsFeed(
    reviews: LazyPagingItems<ReviewUiModel>,
    modifier: Modifier = Modifier,
) {
    val refreshState = reviews.loadState.refresh
    val appendState = reviews.loadState.append
    when {
        appendState is LoadState.Loading -> {
            Box(modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        appendState is LoadState.Error || refreshState is LoadState.Error -> {
            FeedLoadError(
                onRetryClick = reviews::retry,
                modifier = modifier
            )
        }

        appendState is LoadState.NotLoading || refreshState is LoadState.NotLoading -> {
            PullToRefreshBox(
                isRefreshing = refreshState is LoadState.Loading,
                onRefresh = reviews::refresh
            ) {
                LazyColumn(
                    modifier = modifier,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reviews.itemCount) { index ->
                        reviews[index]?.let { review ->
                            ReviewCard(review)
                        }
                    }

                    when (reviews.loadState.append) {
                        LoadState.Loading -> item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is LoadState.Error -> item {
                            FeedLoadError(onRetryClick = reviews::retry)
                        }

                        is LoadState.NotLoading -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewCard(review: ReviewUiModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = review.author,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (review.rating != null) {
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.Star,
                        tint = AppColors.star,
                        contentDescription = stringResource(core.ui.R.string.vote_icon)
                    )

                    Text(
                        text = review.rating,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }

        Text(
            text = review.editedAt,
            modifier = Modifier.padding(top = 2.dp),
            color = MaterialTheme.colorScheme.onBackground.darkenBy(30),
            style = MaterialTheme.typography.bodySmall,
        )

        Text(
            text = review.content,
            modifier = Modifier.padding(top = 10.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
