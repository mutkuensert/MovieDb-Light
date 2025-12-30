package core.domain.profile

interface FavoriteMoviesRefresher {
    suspend operator fun invoke()
}