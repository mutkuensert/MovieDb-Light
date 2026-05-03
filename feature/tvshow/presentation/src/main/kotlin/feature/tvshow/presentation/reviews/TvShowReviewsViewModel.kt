package feature.tvshow.presentation.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import feature.tvshow.domain.TvShowRepository
import feature.tvshow.presentation.detail.model.ReviewUiModel
import feature.tvshow.presentation.detail.model.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TvShowReviewsViewModel(
    tvShowRepository: TvShowRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val tvShowId: Int = requireNotNull(savedStateHandle["id"]) {
        "TvShow id can't be null"
    }

    val reviews: Flow<PagingData<ReviewUiModel>> = tvShowRepository.getReviews(tvShowId)
        .map { pagingData -> pagingData.map { review -> review.toUiModel() } }
        .cachedIn(viewModelScope)
}
