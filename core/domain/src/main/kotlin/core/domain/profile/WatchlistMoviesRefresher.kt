package core.domain.profile

interface WatchlistMoviesRefresher {
    suspend operator fun invoke()
}