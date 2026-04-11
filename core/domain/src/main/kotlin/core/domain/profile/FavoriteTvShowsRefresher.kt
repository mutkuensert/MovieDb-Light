package core.domain.profile

interface FavoriteTvShowsRefresher {
    suspend operator fun invoke()
}
