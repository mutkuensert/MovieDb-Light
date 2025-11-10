package core.domain.profile

interface ProfileFavoriteMoviesPagingInvalidator {
    fun invalidateFavoriteMovies()
}

interface ProfileWatchlistMoviesPagingInvalidator {

    fun invalidateWatchlistMovies()
}

interface ProfileRatedMoviesPagingInvalidator {

    fun invalidateRatedMovies()
}