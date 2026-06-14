package feature.tvshow.domain.datarefresher

import javax.inject.Inject

import core.domain.common.LanguageRelatedDataRefresher
import feature.tvshow.domain.TvShowRepository

class LanguageRelatedDataRefresherImpl @Inject constructor(
    private val tvShowRepository: TvShowRepository,
) : LanguageRelatedDataRefresher {
    override suspend fun invoke() {
        tvShowRepository.updateLanguageRelatedData()
    }
}