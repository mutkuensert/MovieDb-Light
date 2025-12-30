package feature.profile.domain.datarefresher

import core.domain.common.LanguageRelatedDataRefresher
import feature.profile.domain.ProfileRepository

class LanguageRelatedDataRefresherImpl(
    private val profileRepository: ProfileRepository,
) : LanguageRelatedDataRefresher {
    override suspend fun invoke() {
        profileRepository.updateLanguageRelatedData()
    }
}