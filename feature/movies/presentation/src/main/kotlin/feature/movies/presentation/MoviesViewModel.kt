package feature.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import feature.movies.domain.MoviesRepository
import kotlinx.coroutines.flow.map

class MoviesViewModel(
    repository: MoviesRepository,
) : ViewModel() {
    val popularMovies = repository.getPopularMovies().map { pagingData ->
        pagingData.map {
            MovieUiModel(it.id, it.title, it.imageUrl, it.voteAverage)
        }
    }.cachedIn(viewModelScope)
}