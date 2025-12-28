package feature.movie.presentation

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.Failure
import core.domain.profile.MovieRateChangeListener
import feature.movie.domain.MovieRepository

class RemoveRatingUseCase(
    private val movieRateChangeListener: MovieRateChangeListener,
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Int): Result<Unit, Failure> {
        return movieRepository.removeRating(movieId).onSuccess {
            movieRateChangeListener.onMovieRateChanged()
        }
    }
}
