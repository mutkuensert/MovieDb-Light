package feature.profile.domain.datarefresher

import core.domain.profile.FavoriteTvShowsRefresher
import feature.profile.domain.ProfileRepository

class FavoriteTvShowsRefresherImpl(
    private val profileRepository: ProfileRepository,
) : FavoriteTvShowsRefresher {
    override suspend fun invoke() {
        profileRepository.updateFavoriteTvShows()
    }
}
