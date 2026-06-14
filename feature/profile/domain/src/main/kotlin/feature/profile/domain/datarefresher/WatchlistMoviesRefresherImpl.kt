package feature.profile.domain.datarefresher

import javax.inject.Inject

import core.domain.profile.WatchlistMoviesRefresher
import feature.profile.domain.ProfileRepository

class WatchlistMoviesRefresherImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
) : WatchlistMoviesRefresher {
    override suspend fun invoke() {
        profileRepository.updateWatchlistMovies()
    }
}