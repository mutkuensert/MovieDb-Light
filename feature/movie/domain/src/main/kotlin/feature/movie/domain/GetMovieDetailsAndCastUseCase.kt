package feature.movie.domain

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map
import core.domain.ErrorMessage

class GetMovieDetailsAndCastUseCase(private val moviesRepository: MoviesRepository) {

    suspend fun execute(movieId: Int): Result<MovieDetailsAndCast, ErrorMessage> {
        return moviesRepository.getMovieDetails(movieId).flatMap { movieDetails ->
            moviesRepository.getMovieCast(movieId).map { cast ->
                MovieDetailsAndCast(
                    imageUrl = movieDetails.imageUrl,
                    title = movieDetails.title,
                    voteAverage = movieDetails.voteAverage,
                    runtime = movieDetails.runtime,
                    releaseDate = movieDetails.releaseDate,
                    overview = movieDetails.overview,
                    cast = cast
                )
            }
        }
    }
}