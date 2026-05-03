package feature.movie.presentation.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import feature.movie.domain.MovieRepository
import feature.movie.presentation.detail.model.ReviewUiModel
import feature.movie.presentation.detail.model.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieReviewsViewModel(
    movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val movieId: Int = requireNotNull(savedStateHandle["id"]) {
        "Movie id can't be null"
    }

    val reviews: Flow<PagingData<ReviewUiModel>> = movieRepository.getReviewsPagingFlow(movieId)
        .map { pagingData -> pagingData.map { review -> review.toUiModel() } }
        .cachedIn(viewModelScope)
}
