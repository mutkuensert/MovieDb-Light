package feature.profile.domain.datarefresher

import javax.inject.Inject

import core.domain.profile.WatchlistTvShowsRefresher
import feature.profile.domain.ProfileRepository

class WatchlistTvShowsRefresherImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
) : WatchlistTvShowsRefresher {
    override suspend fun invoke() {
        profileRepository.updateWatchlistTvShows()
    }
}
