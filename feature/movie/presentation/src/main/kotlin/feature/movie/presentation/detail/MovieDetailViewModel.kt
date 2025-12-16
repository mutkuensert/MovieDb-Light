package feature.movie.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.domain.AuthStateProvider
import core.domain.movie.SyncMovieFavoriteStatusUseCase
import core.domain.movie.SyncMovieWatchlistStatusUseCase
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.MovieDetailRoute
import feature.movie.domain.MovieRepository
import feature.movie.presentation.R
import feature.movie.presentation.RateMovieUseCase
import feature.movie.presentation.RemoveRatingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import libraries.StrResource
import kotlin.math.roundToInt

class MovieDetailViewModel(
    private val movieRepository: MovieRepository,
    private val rateMovieUseCase: RateMovieUseCase,
    private val removeRatingUseCase: RemoveRatingUseCase,
    private val syncMovieFavoriteStatusUseCase: SyncMovieFavoriteStatusUseCase,
    private val syncMovieWatchlistStatusUseCase: SyncMovieWatchlistStatusUseCase,
    private val loadingAnimator: LoadingAnimator,
    private val popupHandler: PopupHandler,
    private val navigator: Navigator,
    private val authStateProvider: AuthStateProvider,
    private val strResource: StrResource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var movieId: Int = requireNotNull(savedStateHandle["id"]) {
        "Movie id can't be null"
    }
    private val _uiModel = MutableStateFlow(MovieDetailUiModel.initial(movieId))
    val uiModel = _uiModel.asStateFlow()

    val similarMovies = movieRepository.getSimilarMovies(movieId).map {
        it.map { movie ->
            MovieUiModel(
                movie.id,
                movie.title,
                movie.imageUrl,
                movie.voteAverage?.toString(),
                movie.inWatchlist
            )
        }
    }.cachedIn(viewModelScope)

    fun getDetails() {
        viewModelScope.launch {
            loadingAnimator.start()

            movieRepository.getMovieDetails(movieId).onSuccess { movieDetailsAndCast ->
                _uiModel.update {
                    it.copy(
                        imageUrl = movieDetailsAndCast.imageUrl,
                        title = movieDetailsAndCast.title ?: "",
                        vote = movieDetailsAndCast.voteAverage?.toString() ?: "",
                        runtime = movieDetailsAndCast.runtime?.toString() ?: "",
                        year = movieDetailsAndCast.releaseDate?.split("-")
                            ?.firstOrNull() ?: "",
                        overview = movieDetailsAndCast.overview ?: "",
                        cast = emptyList()
                    )
                }
            }.onFailure {
                popupHandler.showSimpleMessage(it)
            }

            movieRepository.getMovieCast(movieId).onSuccess { cast ->
                _uiModel.update {
                    it.copy(cast = cast.map { person -> person.toUiModel() })
                }
            }.onFailure {
                popupHandler.showSimpleMessage(it)
            }

            movieRepository.getProviders(movieId).onSuccess { providers ->
                _uiModel.update {
                    it.copy(providerLogoUrls = providers.mapNotNull { provider -> provider.logoUrl })
                }
            }.onFailure {
                popupHandler.showSimpleMessage(it)
            }

            movieRepository.getTrailerUrl(movieId).onSuccess { url ->
                _uiModel.update {
                    it.copy(trailerUrl = url)
                }
            }.onFailure {
                popupHandler.showSimpleMessage(it)
            }

            if (authStateProvider.loggedIn.value) {
                movieRepository.getAccountStates(movieId).onSuccess { accountStates ->
                    _uiModel.update {
                        it.copy(
                            showRateButton = true,
                            userRate = accountStates.rate?.roundToInt()
                                ?.toString(), //TODO("Implement a rating system supports floating number")
                            inWatchlist = accountStates.watchlist,
                            favorite = accountStates.favorite
                        )
                    }
                }.onFailure {
                    popupHandler.showSimpleMessage(it)
                }
            }

            loadingAnimator.stop()
        }
    }

    fun handleStreamServicesInfoButton() {
        popupHandler.showSimpleMessage(strResource.get(R.string.streaming_services_informations_are_provided_by_justwatch))
    }

    fun handleMovieClick(movieId: Int) {
        navigator.navigateToRoute(MovieDetailRoute(movieId))
    }

    fun handleWatchlistClick(movieId: Int, inWatchlist: Boolean) {
        viewModelScope.launch {
            syncMovieWatchlistStatusUseCase.execute(movieId, inWatchlist).onSuccess {
                _uiModel.update {
                    it.copy(inWatchlist = !uiModel.value.inWatchlist!!)
                }
            }.onFailure {
                popupHandler.showSimpleMessage(it)
            }
        }
    }

    fun handleFavoriteClick() {
        viewModelScope.launch {
            syncMovieFavoriteStatusUseCase.execute(movieId, !uiModel.value.favorite!!)
                .onSuccess {
                    _uiModel.update {
                        it.copy(favorite = !uiModel.value.favorite!!)
                    }
                }.onFailure {
                    popupHandler.showSimpleMessage(it)
                }
        }
    }

    fun handleRateClick(value: Int) {
        viewModelScope.launch {
            rateMovieUseCase.execute(movieId, value).onSuccess {
                _uiModel.update {
                    it.copy(userRate = value.toString())
                }
            }.onFailure {
                popupHandler.showSimpleMessage(it)
            }
        }
    }

    fun handleRemoveRatingClick() {
        viewModelScope.launch {
            removeRatingUseCase.execute(movieId).onSuccess {
                _uiModel.update {
                    it.copy(userRate = null)
                }
            }.onFailure {
                popupHandler.showSimpleMessage(it)
            }
        }
    }
}