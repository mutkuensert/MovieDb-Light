package feature.movie.domain.datarefresher

import javax.inject.Inject

import core.domain.common.LanguageRelatedDataRefresher
import feature.movie.domain.MovieRepository

class LanguageRelatedDataRefresherImpl @Inject constructor(
    private val movieRepository: MovieRepository,
) : LanguageRelatedDataRefresher {
    override suspend fun invoke() {
        movieRepository.updateLanguageRelatedData()
    }
}