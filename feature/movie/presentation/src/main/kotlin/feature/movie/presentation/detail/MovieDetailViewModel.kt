package feature.movie.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.ui.LoadingAnimator
import feature.movie.domain.GetMovieDetailsAndCastUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val getMovieDetailsAndCastUseCase: GetMovieDetailsAndCastUseCase,
    private val loadingAnimator: LoadingAnimator,
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
            getMovieDetailsAndCastUseCase.execute(movieId).onSuccess { movieDetailsAndCast ->
                loadingAnimator.stop()
                _uiModel.update { it ->
                    it.copy(
                        imageUrl = movieDetailsAndCast.imageUrl,
                        title = movieDetailsAndCast.title ?: "",
                        voteAverage = movieDetailsAndCast.voteAverage?.toString() ?: "",
                        runtime = movieDetailsAndCast.runtime?.toString() ?: "",
                        overview = movieDetailsAndCast.overview ?: "",
                        cast = movieDetailsAndCast.cast.map { it.toUiModel() }
                    )
                }
            }.onFailure {
                loadingAnimator.stop()
            }
        }
    }
}