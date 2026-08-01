package feature.person.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onErr
import com.github.michaelbull.result.onOk
import core.domain.common.LanguagePreferenceUpdateState
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.MovieDetailRoute
import core.ui.route.TvShowDetailRoute
import core.ui.showFailurePopup
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.person.domain.PersonRepository
import feature.person.presentation.detail.model.PersonDetailUiModel
import feature.person.presentation.detail.model.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val loadingAnimator: LoadingAnimator,
    private val popupHandler: PopupHandler,
    private val navigator: Navigator,
    private val languagePreferenceUpdateState: LanguagePreferenceUpdateState,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var personId: Int = requireNotNull(savedStateHandle["id"]) {
        "Person id can't be null"
    }
    private val _uiModel = savedStateHandle.getMutableStateFlow(
        "uiModel",
        PersonDetailUiModel.initial(personId)
    )
    val uiModel = _uiModel.asStateFlow()

    init {
        viewModelScope.launch {
            languagePreferenceUpdateState.updatedLanguage.collectLatest {
                getDetails()
            }
        }
    }

    fun getDetails() {
        viewModelScope.launch {
            loadingAnimator.start()
            val jobs = mutableListOf<Job>()

            jobs.add(launch {
                personRepository.getPersonDetails(personId).onOk { personDetails ->
                    _uiModel.update {
                        it.copy(
                            imagePath = personDetails.imagePath,
                            name = personDetails.name,
                            knownForDepartment = personDetails.knownForDepartment ?: "",
                            birthday = personDetails.birthday ?: "",
                            deathday = personDetails.deathday ?: "",
                            placeOfBirth = personDetails.placeOfBirth ?: "",
                            biography = personDetails.biography ?: ""
                        )
                    }
                }.onErr(popupHandler::showFailurePopup)
            })

            jobs.add(launch {
                personRepository.getPersonMovieCredits(personId).onOk { credits ->
                    _uiModel.update {
                        it.copy(
                            castMovies = credits.cast.map { movie -> movie.toUiModel() },
                            crewMovies = credits.crew.map { movie -> movie.toUiModel() }
                        )
                    }
                }
            })

            jobs.add(launch {
                personRepository.getPersonTvCredits(personId).onOk { credits ->
                    _uiModel.update {
                        it.copy(
                            castTvShows = credits.cast.map { tvShow -> tvShow.toUiModel() },
                            crewTvShows = credits.crew.map { movie -> movie.toUiModel() }
                        )
                    }
                }
            })

            jobs.joinAll()
            loadingAnimator.stop()
        }
    }

    fun handleMovieClick(movieId: Int) {
        navigator.navigateToRoute(MovieDetailRoute(movieId))
    }

    fun handleTvShowClick(tvShowId: Int) {
        navigator.navigateToRoute(TvShowDetailRoute(tvShowId))
    }
}
