package feature.movie.presentation

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import core.domain.ErrorMessage
import core.domain.profile.ProfileRatedMoviesPagingInvalidator
import feature.movie.domain.MovieRepository

class RateMovieUseCase(
    private val profileRatedMoviesPagingInvalidator: ProfileRatedMoviesPagingInvalidator,
    private val movieRepository: MovieRepository,
) {
    suspend fun execute(movieId: Int, rating: Int): Result<Unit, ErrorMessage> {
        return movieRepository.rateMovie(movieId, rating).onSuccess {
            profileRatedMoviesPagingInvalidator.invalidateRatedMovies()
        }
    }
}
