package feature.movie.domain.usecase

import javax.inject.Inject

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onOk
import core.domain.Failure
import core.domain.profile.RatedMoviesRefresher
import feature.movie.domain.MovieRepository

class RemoveRatingUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val ratedMoviesRefresher: RatedMoviesRefresher,
) {
    suspend operator fun invoke(movieId: Int): Result<Unit, Failure> {
        return movieRepository.removeRating(movieId).onOk {
            ratedMoviesRefresher()
        }
    }
}
