package feature.movie.presentation

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.Failure
import core.domain.profile.RatedMoviesRefresher
import feature.movie.domain.MovieRepository

class RemoveRatingUseCase(
    private val ratedMoviesRefresher: RatedMoviesRefresher,
    private val movieRepository: MovieRepository,
) {
    suspend fun execute(movieId: Int): Result<Unit, Failure> {
        return movieRepository.removeRating(movieId).onSuccess {
            ratedMoviesRefresher.refreshRatedMovies()
        }
    }
}
