package feature.movie.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import core.ui.navigation.Navigator
import core.ui.route.MovieDetailRoute
import feature.movie.domain.MovieRepository
import feature.movie.domain.model.Movie
import feature.movie.domain.usecase.SyncMovieWatchlistStatusUseCase
import feature.movie.presentation.list.model.MovieUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MoviesViewModel(
    repository: MovieRepository,
    private val syncMovieWatchlistStatusUseCase: SyncMovieWatchlistStatusUseCase,
    private val navigator: Navigator,
) : ViewModel() {
    val popularMovies = repository.getPopularMovies(null).asUiModelFlow()
    val moviesNowPlaying = repository.getMoviesNowPlaying(null).asUiModelFlow()
    val topRatedMovies = repository.getTopRatedMovies(null).asUiModelFlow()
    val upcomingMovies = repository.getUpcomingMovies(null).asUiModelFlow()

    private fun Flow<PagingData<Movie>>.asUiModelFlow(): Flow<PagingData<MovieUiModel>> {
        return map { pagingData ->
            pagingData.map {
                MovieUiModel(
                    it.id,
                    it.title,
                    it.imagePath,
                    it.voteAverage?.toString(),
                    it.inWatchlist
                )
            }
        }
    }

    fun handleWatchlistClick(movie: MovieUiModel) {
        val inWatchlist = requireNotNull(movie.inWatchlist) {
            "Can't be null if button is visible"
        }
        viewModelScope.launch {
            syncMovieWatchlistStatusUseCase(movie.id, !inWatchlist)
        }
    }

    fun handleMovieClick(movieId: Int) {
        navigator.navigateToRoute(MovieDetailRoute(movieId))
    }
}

private fun emptyPagingDataFlow(): Flow<PagingData<MovieUiModel>> = flowOf(PagingData.empty())