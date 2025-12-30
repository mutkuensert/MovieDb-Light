package feature.profile.domain.datarefresher

import core.domain.profile.FavoriteMoviesRefresher
import feature.profile.domain.ProfileRepository

class FavoriteMoviesRefresherImpl(
    private val profileRepository: ProfileRepository,
) : FavoriteMoviesRefresher {
    override suspend fun invoke() {
        profileRepository.updateFavoriteMovies()
    }
}