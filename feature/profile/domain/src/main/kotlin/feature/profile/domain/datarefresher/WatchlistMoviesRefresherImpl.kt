package feature.profile.domain.datarefresher

import core.domain.profile.WatchlistMoviesRefresher
import feature.profile.domain.ProfileRepository

class WatchlistMoviesRefresherImpl(
    private val profileRepository: ProfileRepository,
) : WatchlistMoviesRefresher {
    override suspend fun invoke() {
        profileRepository.updateWatchlistMovies()
    }
}