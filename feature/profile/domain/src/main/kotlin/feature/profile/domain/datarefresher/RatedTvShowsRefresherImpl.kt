package feature.profile.domain.datarefresher

import javax.inject.Inject

import core.domain.profile.RatedTvShowsRefresher
import feature.profile.domain.ProfileRepository

class RatedTvShowsRefresherImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
) : RatedTvShowsRefresher {
    override suspend fun invoke() {
        profileRepository.updateRatedTvShows()
    }
}
