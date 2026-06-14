package feature.profile.domain.datarefresher

import javax.inject.Inject

import core.domain.profile.FavoriteMoviesRefresher
import feature.profile.domain.ProfileRepository

class FavoriteMoviesRefresherImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
) : FavoriteMoviesRefresher {
    override suspend fun invoke() {
        profileRepository.updateFavoriteMovies()
    }
}