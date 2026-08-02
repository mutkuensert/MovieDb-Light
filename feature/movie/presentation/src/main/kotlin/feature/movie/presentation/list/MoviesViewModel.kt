package feature.movie.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import core.ui.navigation.Navigator
import core.ui.route.MovieDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.movie.domain.MovieRepository
import feature.movie.domain.model.Movie
import feature.movie.presentation.list.model.MovieUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor(
    repository: MovieRepository,
    private val navigator: Navigator,
) : ViewModel() {
    val popularMovies = repository.getPopularMovies().cachedIn(viewModelScope).asUiModelFlow()
    val moviesNowPlaying = repository.getMoviesNowPlaying().cachedIn(viewModelScope).asUiModelFlow()
    val topRatedMovies = repository.getTopRatedMovies().cachedIn(viewModelScope).asUiModelFlow()
    val upcomingMovies = repository.getUpcomingMovies().cachedIn(viewModelScope).asUiModelFlow()

    private fun Flow<PagingData<Movie>>.asUiModelFlow(): Flow<PagingData<MovieUiModel>> {
        return map { pagingData ->
            pagingData.map {
                MovieUiModel(
                    it.id,
                    it.title,
                    it.imagePath,
                    it.voteAverage?.toString(),
                )
            }
        }
    }

    fun handleMovieClick(movieId: Int) {
        navigator.navigateToRoute(MovieDetailRoute(movieId))
    }
}
