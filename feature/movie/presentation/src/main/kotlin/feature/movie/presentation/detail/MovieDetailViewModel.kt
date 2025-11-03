package feature.movie.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.ui.LoadingAnimator
import core.ui.PopupHandler
import feature.movie.domain.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository,
    private val loadingAnimator: LoadingAnimator,
    private val popupHandler: PopupHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var movieId: Int = requireNotNull(savedStateHandle["id"]) {
        "Movie id can't be null"
    }
    private val _uiModel = MutableStateFlow(MovieDetailUiModel.initial())
    val uiModel = _uiModel.asStateFlow()

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
            }

            loadingAnimator.stop()
        }
    }

    fun handleStreamServicesInfoButton() {
        popupHandler.showSimpleMessage("Streaming services informations are provided by JustWatch.")
    }
}