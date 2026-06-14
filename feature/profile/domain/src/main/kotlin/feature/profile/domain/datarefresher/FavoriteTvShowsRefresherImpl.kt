package feature.profile.domain.datarefresher

import javax.inject.Inject

import core.domain.profile.FavoriteTvShowsRefresher
import feature.profile.domain.ProfileRepository

class FavoriteTvShowsRefresherImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
) : FavoriteTvShowsRefresher {
    override suspend fun invoke() {
        profileRepository.updateFavoriteTvShows()
    }
}
