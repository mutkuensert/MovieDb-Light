package feature.movie.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import core.domain.AccountRepository
import core.ui.navigation.Navigator
import feature.movie.domain.Movie
import feature.movie.domain.MovieRepository
import feature.movie.presentation.detail.MovieDetailRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val repository: MovieRepository,
    private val accountRepository: AccountRepository,
    private val navigator: Navigator,
) : ViewModel() {
    private val _uiModel = MutableStateFlow(MoviesUiModel.initial())
    val uiModel: StateFlow<MoviesUiModel> = _uiModel.asStateFlow()

    val popularMovies = getPagingDataWithCountry { repository.getPopularMovies(it) }
    val moviesNowPlaying = getPagingDataWithCountry { repository.getMoviesNowPlaying(it) }
    val topRatedMovies = getPagingDataWithCountry { repository.getTopRatedMovies(it) }
    val upcomingMovies = getPagingDataWithCountry { repository.getUpcomingMovies(it) }

    private fun PagingData<Movie>.toUiModel(): PagingData<MovieUiModel> {
        return map {
            MovieUiModel(
                it.id,
                it.title,
                it.imageUrl,
                it.voteAverage.toString(),
                it.isFavorite
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getPagingDataWithCountry(getPagingData: suspend (country: String?) -> Flow<PagingData<Movie>>): Flow<PagingData<MovieUiModel>> {
        return uiModel.distinctUntilChanged { old, new -> old.selectedCountry == new.selectedCountry }
            .flatMapLatest { uiModel ->
                getPagingData(uiModel.selectedCountry)
                    .map { it.toUiModel() }
            }.cachedIn(viewModelScope)
    }

    fun handleCountryClick(country: String) {
        _uiModel.update {
            it.copy(selectedCountry = country, isCountryDialogVisible = false)
        }
    }

    fun handleDismissCountryDialog() {
        _uiModel.update {
            it.copy(isCountryDialogVisible = false)
        }
    }

    fun handleOpenCountryDialogClick() {
        if (uiModel.value.selectedCountry.isNullOrEmpty()) {
            _uiModel.update {
                it.copy(isCountryDialogVisible = true)
            }
        } else {
            _uiModel.update {
                it.copy(selectedCountry = null)
            }
        }
    }

    fun handleFavoriteClick(movie: MovieUiModel) {
        val isFavorite = requireNotNull(movie.isFavorite) {
            "Can't be null if button is visible"
        }
        viewModelScope.launch {
            accountRepository.syncMovieFavoriteStatus(!isFavorite, movie.id)
        }
    }

    fun handleMovieClick(movieId: Int) {
        navigator.navigateToRoute(MovieDetailRoute(movieId))
    }
}