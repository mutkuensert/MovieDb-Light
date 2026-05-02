package feature.movie.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onErr
import com.github.michaelbull.result.onOk
import core.domain.auth.AuthStateProvider
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.PersonDetailRoute
import core.ui.showFailurePopup
import feature.movie.domain.MovieRepository
import feature.movie.domain.usecase.RateMovieUseCase
import feature.movie.domain.usecase.RemoveRatingUseCase
import feature.movie.domain.usecase.SyncMovieFavoriteStatusUseCase
import feature.movie.domain.usecase.SyncMovieWatchlistStatusUseCase
import feature.movie.presentation.R
import feature.movie.presentation.detail.model.MovieDetailUiModel
import feature.movie.presentation.detail.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.stringresource.StringResource
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
    private val stringResource: StringResource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var movieId: Int = requireNotNull(savedStateHandle["id"]) {
        "Movie id can't be null"
    }
    private val _uiModel = MutableStateFlow(MovieDetailUiModel.initial(movieId))
    val uiModel = _uiModel.asStateFlow()

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

            movieRepository.getMovieDetails(movieId).onOk { movieDetails ->
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
            }.onErr(popupHandler::showFailurePopup)

            movieRepository.getImagePaths(movieId).onOk { paths ->
                _uiModel.update {
                    it.copy(imagePaths = it.imagePaths + paths)
                }
            }

            movieRepository.getPeople(movieId).onOk { people ->
                _uiModel.update {
                    it.copy(
                        cast = people.cast.map { person -> person.toUiModel() },
                        directors = people.directors.map { director -> director.toUiModel() },
                        writers = people.writers.map { writer -> writer.toUiModel() }
                    )
                }
            }

            movieRepository.getProviders(movieId).onOk { providers ->
                _uiModel.update {
                    it.copy(providerLogoPaths = providers.mapNotNull { provider -> provider.logoPath })
                }
            }

            movieRepository.getTrailerYoutubeVideoId(movieId).onOk { id ->
                _uiModel.update {
                    it.copy(youtubeVideoId = id)
                }
            }

            movieRepository.getReviews(movieId).onOk { reviews ->
                _uiModel.update {
                    it.copy(reviews = reviews.map { review -> review.toUiModel() })
                }
            }

            if (authStateProvider.loggedIn.value) {
                movieRepository.getAccountStates(movieId).onOk { accountStates ->
                    _uiModel.update {
                        it.copy(
                            userRate = accountStates.rate?.roundToInt()
                                ?.toString(), //TODO Implement a rating system supports floating number
                            inWatchlist = accountStates.watchlist,
                            favorite = accountStates.favorite
                        )
                    }
                }.onErr(popupHandler::showFailurePopup)
            }

            loadingAnimator.stop()
        }
    }

    fun handleStreamServicesInfoButton() {
        popupHandler.showSimpleMessage(stringResource.get(R.string.streaming_services_information_are_provided_by_justwatch))
    }

    fun handlePersonClick(personId: Int) {
        navigator.navigateToRoute(PersonDetailRoute(personId))
    }

    fun handleWatchlistClick(movieId: Int, inWatchlist: Boolean) {
        viewModelScope.launch {
            syncMovieWatchlistStatusUseCase(movieId, inWatchlist).onOk {
                _uiModel.update {
                    it.copy(inWatchlist = !uiModel.value.inWatchlist!!)
                }
            }.onErr(popupHandler::showFailurePopup)
        }
    }

    fun handleFavoriteClick() {
        viewModelScope.launch {
            syncMovieFavoriteStatusUseCase(movieId, !uiModel.value.favorite!!)
                .onOk {
                    _uiModel.update {
                        it.copy(favorite = !uiModel.value.favorite!!)
                    }
                }.onErr(popupHandler::showFailurePopup)
        }
    }

    fun handleRateClick(value: Int) {
        viewModelScope.launch {
            rateMovieUseCase(movieId, value).onOk {
                _uiModel.update {
                    it.copy(userRate = value.toString())
                }
            }.onErr(popupHandler::showFailurePopup)
        }
    }

    fun handleRemoveRatingClick() {
        viewModelScope.launch {
            removeRatingUseCase(movieId).onOk {
                _uiModel.update {
                    it.copy(userRate = null)
                }
            }.onErr(popupHandler::showFailurePopup)
        }
    }
}
