package feature.tvshow.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onErr
import com.github.michaelbull.result.onOk
import core.domain.auth.AuthStateProvider
import core.domain.common.LanguagePreferenceUpdateState
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.PersonDetailRoute
import core.ui.route.TvShowReviewsRoute
import core.ui.showFailurePopup
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.tvshow.domain.TvShowRepository
import feature.tvshow.domain.usecase.RateTvShowUseCase
import feature.tvshow.domain.usecase.RemoveRatingUseCase
import feature.tvshow.domain.usecase.SyncTvShowFavoriteStatusUseCase
import feature.tvshow.domain.usecase.SyncTvShowWatchlistStatusUseCase
import feature.tvshow.presentation.R
import feature.tvshow.presentation.detail.model.TvShowDetailUiModel
import feature.tvshow.presentation.detail.model.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import utils.stringresource.StringResource
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class TvShowDetailViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val rateTvShowUseCase: RateTvShowUseCase,
    private val removeRatingUseCase: RemoveRatingUseCase,
    private val syncTvShowFavoriteStatusUseCase: SyncTvShowFavoriteStatusUseCase,
    private val syncTvShowWatchlistStatusUseCase: SyncTvShowWatchlistStatusUseCase,
    private val loadingAnimator: LoadingAnimator,
    private val popupHandler: PopupHandler,
    private val navigator: Navigator,
    private val authStateProvider: AuthStateProvider,
    private val stringResource: StringResource,
    private val languagePreferenceUpdateState: LanguagePreferenceUpdateState,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var tvShowId: Int = requireNotNull(savedStateHandle["id"]) {
        "TvShow id can't be null"
    }
    private val _uiModel = savedStateHandle.getMutableStateFlow(
        "uiModel",
        TvShowDetailUiModel.initial(tvShowId)
    )
    val uiModel = _uiModel.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
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

            launch {
                languagePreferenceUpdateState.updatedLanguage.collectLatest {
                    getDetails()
                }
            }
        }
    }

    fun getDetails() {
        viewModelScope.launch {
            loadingAnimator.start()
            val jobs = mutableListOf<Job>()

            jobs.add(launch {
                tvShowRepository.getTvShowDetails(tvShowId).onOk { tvShowDetails ->
                    _uiModel.update {
                        it.copy(
                            imagePaths = tvShowDetails.imagePath?.let { path -> listOf(path) }
                                ?: listOf(),
                            title = tvShowDetails.title ?: "",
                            vote = tvShowDetails.voteAverage?.toString() ?: "",
                            runtime = tvShowDetails.runtime?.toString() ?: "",
                            releaseDate = tvShowDetails.releaseDate?.replace("-", ".") ?: "",
                            genres = tvShowDetails.genres.joinToString(", "),
                            overview = tvShowDetails.overview ?: "",
                            cast = emptyList()
                        )
                    }
                }.onErr(popupHandler::showFailurePopup)
            })

            jobs.add(launch {
                tvShowRepository.getImagePaths(tvShowId).onOk { paths ->
                    _uiModel.update {
                        it.copy(imagePaths = it.imagePaths + paths)
                    }
                }
            })

            jobs.add(launch {
                tvShowRepository.getCast(tvShowId).onOk { cast ->
                    _uiModel.update {
                        it.copy(
                            cast = cast.map { person -> person.toUiModel() },
                        )
                    }
                }
            })

            jobs.add(launch {
                tvShowRepository.getProviders(tvShowId).onOk { providers ->
                    _uiModel.update {
                        it.copy(providerLogoPaths = providers.mapNotNull { provider -> provider.logoPath })
                    }
                }
            })

            jobs.add(launch {
                tvShowRepository.getTrailerYoutubeVideoId(tvShowId).onOk { id ->
                    _uiModel.update {
                        it.copy(trailerYoutubeVideoId = id)
                    }
                }
            })

            jobs.add(launch {
                tvShowRepository.getReviews(tvShowId).onOk { reviews ->
                    _uiModel.update {
                        it.copy(
                            review = reviews.firstOrNull()?.toUiModel(),
                            showsReviewsButton = reviews.count() > 2
                        )
                    }
                }
            })

            jobs.add(launch {
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
            })

            jobs.joinAll()
            loadingAnimator.stop()
        }
    }

    fun handleStreamServicesInfoButton() {
        popupHandler.showSimpleMessage(stringResource.get(R.string.streaming_services_information_are_provided_by_justwatch))
    }

    fun handlePersonClick(personId: Int) {
        navigator.navigateToRoute(PersonDetailRoute(personId))
    }

    fun handleReviewsClick() {
        navigator.navigateToRoute(TvShowReviewsRoute(tvShowId))
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
