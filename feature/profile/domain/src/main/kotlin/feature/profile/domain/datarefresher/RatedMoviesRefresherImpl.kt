package feature.profile.domain.datarefresher

import javax.inject.Inject

import core.domain.profile.RatedMoviesRefresher
import feature.profile.domain.ProfileRepository

class RatedMoviesRefresherImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
) : RatedMoviesRefresher {
    override suspend fun invoke() {
        profileRepository.updateRatedMovies()
    }
}