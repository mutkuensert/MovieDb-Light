package feature.movie.domain.datarefresher

import core.domain.common.LanguageRelatedDataRefresher
import feature.movie.domain.MovieRepository

class LanguageRelatedDataRefresherImpl(
    private val movieRepository: MovieRepository,
) : LanguageRelatedDataRefresher {
    override suspend fun invoke() {
        movieRepository.updateLanguageRelatedData()
    }
}