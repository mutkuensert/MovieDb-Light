package feature.tvshow.domain.datarefresher

import core.domain.common.LanguageRelatedDataRefresher
import feature.tvshow.domain.TvShowRepository

class LanguageRelatedDataRefresherImpl(
    private val tvShowRepository: TvShowRepository,
) : LanguageRelatedDataRefresher {
    override suspend fun invoke() {
        tvShowRepository.updateLanguageRelatedData()
    }
}