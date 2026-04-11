package feature.profile.domain.datarefresher

import core.domain.profile.RatedTvShowsRefresher
import feature.profile.domain.ProfileRepository

class RatedTvShowsRefresherImpl(
    private val profileRepository: ProfileRepository,
) : RatedTvShowsRefresher {
    override suspend fun invoke() {
        profileRepository.updateRatedTvShows()
    }
}
