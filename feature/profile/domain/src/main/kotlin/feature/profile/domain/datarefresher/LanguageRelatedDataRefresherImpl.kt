package feature.profile.domain.datarefresher

import javax.inject.Inject

import core.domain.common.LanguageRelatedDataRefresher
import feature.profile.domain.ProfileRepository

class LanguageRelatedDataRefresherImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
) : LanguageRelatedDataRefresher {
    override suspend fun invoke() {
        profileRepository.updateLanguageRelatedData()
    }
}