package feature.movie.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onOk
import core.domain.Failure
import core.domain.profile.RatedMoviesRefresher
import feature.movie.domain.MovieRepository

class RateMovieUseCase(
    private val movieRepository: MovieRepository,
    private val ratedMoviesRefresher: RatedMoviesRefresher,
) {
    suspend operator fun invoke(movieId: Int, rating: Int): Result<Unit, Failure> {
        return movieRepository.rateMovie(movieId, rating).onOk {
            ratedMoviesRefresher()
        }
    }
}
