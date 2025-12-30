package core.domain.profile

interface RatedMoviesRefresher {
    suspend operator fun invoke()
}