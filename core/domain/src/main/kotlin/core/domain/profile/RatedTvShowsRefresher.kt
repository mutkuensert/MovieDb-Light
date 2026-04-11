package core.domain.profile

interface RatedTvShowsRefresher {
    suspend operator fun invoke()
}
