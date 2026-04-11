package feature.movie.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.domain.auth.AuthStateProvider
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.MovieDetailRoute
import core.ui.route.PersonDetailRoute
import core.ui.showFailurePopup
import feature.movie.domain.MovieRepository
import feature.movie.domain.usecase.RateMovieUseCase
import feature.movie.domain.usecase.RemoveRatingUseCase
import feature.movie.domain.usecase.SyncMovieFavoriteStatusUseCase
import feature.movie.domain.usecase.SyncMovieWatchlistStatusUseCase
import feature.movie.presentation.R
import feature.movie.presentation.detail.model.MovieDetailUiModel
import feature.movie.presentation.detail.model.MovieUiModel
import feature.movie.presentation.detail.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import libraries.stringresource.StrResource
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
                movie.imagePath,
                movie.voteAverage?.toString(),
                movie.inWatchlist
            )
        }
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            authStateProvider.loggedIn.collectLatest { loggedIn ->
                _uiModel.update {
                    it.copy(
                        showRateButton = loggedIn,
                        showWatchlistButton = loggedIn,
                        showFavoriteButton = loggedIn
                    )
                }
            }
        }
    }

    fun getDetails() {
        viewModelScope.launch {
            loadingAnimator.start()

            movieRepository.getMovieDetails(movieId).onSuccess { movieDetails ->
                _uiModel.update {
                    it.copy(
                        imagePaths = movieDetails.imagePath?.let { path -> listOf(path) }
                            ?: listOf(),
                        title = movieDetails.title ?: "",
                        vote = movieDetails.voteAverage?.toString() ?: "",
                        runtime = movieDetails.runtime?.toString() ?: "",
                        year = movieDetails.releaseDate?.split("-")
                            ?.firstOrNull() ?: "",
                        genres = movieDetails.genres.joinToString(", "),
                        overview = movieDetails.overview ?: "",
                        cast = emptyList()
                    )
                }
            }.onFailure(popupHandler::showFailurePopup)

            movieRepository.getImagePaths(movieId).onSuccess { paths ->
                _uiModel.update {
                    it.copy(imagePaths = it.imagePaths + paths)
                }
            }

            movieRepository.getPeople(movieId).onSuccess { people ->
                _uiModel.update {
                    it.copy(
                        cast = people.cast.map { person -> person.toUiModel() },
                        directors = people.directors.map { director -> director.toUiModel() },
                        writers = people.writers.map { writer -> writer.toUiModel() }
                    )
                }
            }

            movieRepository.getProviders(movieId).onSuccess { providers ->
                _uiModel.update {
                    it.copy(providerLogoPaths = providers.mapNotNull { provider -> provider.logoPath })
                }
            }

            movieRepository.getTrailerUrl(movieId).onSuccess { url ->
                _uiModel.update {
                    it.copy(trailerUrl = url)
                }
            }

            if (authStateProvider.loggedIn.value) {
                movieRepository.getAccountStates(movieId).onSuccess { accountStates ->
                    _uiModel.update {
                        it.copy(
                            userRate = accountStates.rate?.roundToInt()
                                ?.toString(), //TODO Implement a rating system supports floating number
                            inWatchlist = accountStates.watchlist,
                            favorite = accountStates.favorite
                        )
                    }
                }.onFailure(popupHandler::showFailurePopup)
            }

            loadingAnimator.stop()
        }
    }

    fun handleStreamServicesInfoButton() {
        popupHandler.showSimpleMessage(strResource.get(R.string.streaming_services_information_are_provided_by_justwatch))
    }

    fun handleMovieClick(movieId: Int) {
        navigator.navigateToRoute(MovieDetailRoute(movieId))
    }

    fun handlePersonClick(personId: Int) {
        navigator.navigateToRoute(PersonDetailRoute(personId))
    }

    fun handleWatchlistClick(movieId: Int, inWatchlist: Boolean) {
        viewModelScope.launch {
            syncMovieWatchlistStatusUseCase(movieId, inWatchlist).onSuccess {
                _uiModel.update {
                    it.copy(inWatchlist = !uiModel.value.inWatchlist!!)
                }
            }.onFailure(popupHandler::showFailurePopup)
        }
    }

    fun handleFavoriteClick() {
        viewModelScope.launch {
            syncMovieFavoriteStatusUseCase(movieId, !uiModel.value.favorite!!)
                .onSuccess {
                    _uiModel.update {
                        it.copy(favorite = !uiModel.value.favorite!!)
                    }
                }.onFailure(popupHandler::showFailurePopup)
        }
    }

    fun handleRateClick(value: Int) {
        viewModelScope.launch {
            rateMovieUseCase(movieId, value).onSuccess {
                _uiModel.update {
                    it.copy(userRate = value.toString())
                }
            }.onFailure(popupHandler::showFailurePopup)
        }
    }

    fun handleRemoveRatingClick() {
        viewModelScope.launch {
            removeRatingUseCase(movieId).onSuccess {
                _uiModel.update {
                    it.copy(userRate = null)
                }
            }.onFailure(popupHandler::showFailurePopup)
        }
    }
}
