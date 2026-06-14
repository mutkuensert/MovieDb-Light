package feature.movie.presentation.reviews

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
import androidx.compose.material3.TextButton
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
import core.ui.darkenBy
import feature.movie.presentation.R
import feature.movie.presentation.detail.model.ReviewUiModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun MovieReviewsScreen(viewModel: MovieReviewsViewModel = hiltViewModel()) {
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
    when (reviews.loadState.refresh) {
        LoadState.Loading -> Box(modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is LoadState.Error -> LoadError(
            onRetryClick = reviews::retry,
            modifier = modifier
        )

        is LoadState.NotLoading -> {
            if (reviews.itemCount == 0) {
                Box(modifier, contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.no_reviews),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                return
            }

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
                        LoadError(onRetryClick = reviews::retry)
                    }

                    is LoadState.NotLoading -> Unit
                }
            }
        }
    }
}

@Composable
private fun LoadError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.reviews_could_not_be_loaded),
            color = MaterialTheme.colorScheme.onBackground
        )
        TextButton(onClick = onRetryClick) {
            Text(stringResource(R.string.retry))
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
