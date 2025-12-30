package feature.profile.domain.datarefresher

import core.domain.profile.RatedMoviesRefresher
import feature.profile.domain.ProfileRepository

class RatedMoviesRefresherImpl(
    private val profileRepository: ProfileRepository,
) : RatedMoviesRefresher {
    override suspend fun invoke() {
        profileRepository.updateRatedMovies()
    }
}