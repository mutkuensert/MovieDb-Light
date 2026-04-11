package core.domain.profile

interface WatchlistTvShowsRefresher {
    suspend operator fun invoke()
}
