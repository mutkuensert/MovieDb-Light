package feature.tvshow.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.michaelbull.result.onErr
import com.github.michaelbull.result.onOk
import core.domain.auth.AuthStateProvider
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.PersonDetailRoute
import core.ui.route.TvShowDetailRoute
import core.ui.showFailurePopup
import feature.tvshow.domain.TvShowRepository
import feature.tvshow.domain.usecase.RateTvShowUseCase
import feature.tvshow.domain.usecase.RemoveRatingUseCase
import feature.tvshow.domain.usecase.SyncTvShowFavoriteStatusUseCase
import feature.tvshow.domain.usecase.SyncTvShowWatchlistStatusUseCase
import feature.tvshow.presentation.R
import feature.tvshow.presentation.detail.model.TvShowDetailUiModel
import feature.tvshow.presentation.detail.model.TvShowUiModel
import feature.tvshow.presentation.detail.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.stringresource.StrResource
import kotlin.math.roundToInt

class TvShowDetailViewModel(
    private val tvShowRepository: TvShowRepository,
    private val rateTvShowUseCase: RateTvShowUseCase,
    private val removeRatingUseCase: RemoveRatingUseCase,
    private val syncTvShowFavoriteStatusUseCase: SyncTvShowFavoriteStatusUseCase,
    private val syncTvShowWatchlistStatusUseCase: SyncTvShowWatchlistStatusUseCase,
    private val loadingAnimator: LoadingAnimator,
    private val popupHandler: PopupHandler,
    private val navigator: Navigator,
    private val authStateProvider: AuthStateProvider,
    private val strResource: StrResource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var tvShowId: Int = requireNotNull(savedStateHandle["id"]) {
        "TvShow id can't be null"
    }
    private val _uiModel = MutableStateFlow(TvShowDetailUiModel.initial(tvShowId))
    val uiModel = _uiModel.asStateFlow()

    val similarTvShows = tvShowRepository.getSimilarTvShows(tvShowId).map {
        it.map { tvShow ->
            TvShowUiModel(
                tvShow.id,
                tvShow.title,
                tvShow.imagePath,
                tvShow.voteAverage?.toString(),
                tvShow.inWatchlist
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

            tvShowRepository.getTvShowDetails(tvShowId).onOk { tvShowDetails ->
                _uiModel.update {
                    it.copy(
                        imagePaths = tvShowDetails.imagePath?.let { path -> listOf(path) }
                            ?: listOf(),
                        title = tvShowDetails.title ?: "",
                        vote = tvShowDetails.voteAverage?.toString() ?: "",
                        runtime = tvShowDetails.runtime?.toString() ?: "",
                        year = tvShowDetails.releaseDate?.split("-")
                            ?.firstOrNull() ?: "",
                        genres = tvShowDetails.genres.joinToString(", "),
                        overview = tvShowDetails.overview ?: "",
                        cast = emptyList()
                    )
                }
            }.onErr(popupHandler::showFailurePopup)

            tvShowRepository.getImagePaths(tvShowId).onOk { paths ->
                _uiModel.update {
                    it.copy(imagePaths = it.imagePaths + paths)
                }
            }

            tvShowRepository.getCast(tvShowId).onOk { cast ->
                _uiModel.update {
                    it.copy(
                        cast = cast.map { person -> person.toUiModel() },
                    )
                }
            }

            tvShowRepository.getProviders(tvShowId).onOk { providers ->
                _uiModel.update {
                    it.copy(providerLogoPaths = providers.mapNotNull { provider -> provider.logoPath })
                }
            }

            tvShowRepository.getTrailerYoutubeVideoId(tvShowId).onOk { id ->
                _uiModel.update {
                    it.copy(trailerYoutubeVideoId = id)
                }
            }

            if (authStateProvider.loggedIn.value) {
                tvShowRepository.getAccountStates(tvShowId).onOk { accountStates ->
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
        popupHandler.showSimpleMessage(strResource.get(R.string.streaming_services_information_are_provided_by_justwatch))
    }

    fun handleTvShowClick(tvShowId: Int) {
        navigator.navigateToRoute(TvShowDetailRoute(tvShowId))
    }

    fun handlePersonClick(personId: Int) {
        navigator.navigateToRoute(PersonDetailRoute(personId))
    }

    fun handleWatchlistClick(tvShowId: Int, inWatchlist: Boolean) {
        viewModelScope.launch {
            syncTvShowWatchlistStatusUseCase(tvShowId, inWatchlist).onOk {
                _uiModel.update {
                    it.copy(inWatchlist = !uiModel.value.inWatchlist!!)
                }
            }.onErr(popupHandler::showFailurePopup)
        }
    }

    fun handleFavoriteClick() {
        viewModelScope.launch {
            syncTvShowFavoriteStatusUseCase(tvShowId, !uiModel.value.favorite!!).onOk {
                _uiModel.update {
                    it.copy(favorite = !uiModel.value.favorite!!)
                }
            }.onErr(popupHandler::showFailurePopup)
        }
    }

    fun handleRateClick(value: Int) {
        viewModelScope.launch {
            rateTvShowUseCase(tvShowId, value).onOk {
                _uiModel.update {
                    it.copy(userRate = value.toString())
                }
            }.onErr(popupHandler::showFailurePopup)
        }
    }

    fun handleRemoveRatingClick() {
        viewModelScope.launch {
            removeRatingUseCase(tvShowId).onOk {
                _uiModel.update {
                    it.copy(userRate = null)
                }
            }.onErr(popupHandler::showFailurePopup)
        }
    }
}
