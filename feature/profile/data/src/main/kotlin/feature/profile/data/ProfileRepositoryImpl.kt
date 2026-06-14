package feature.profile.data

import javax.inject.Inject
import javax.inject.Singleton

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import core.data.SessionManager
import core.data.account.AccountService
import core.data.account.model.toDto
import core.data.util.withDecimals
import core.database.LanguagePreference
import core.database.account.FavoriteMovieDao
import core.database.account.FavoriteTvShowDao
import core.database.account.RatedMovieDao
import core.database.account.RatedTvShowDao
import core.database.account.WatchlistMovieDao
import core.database.account.WatchlistTvShowDao
import core.domain.account.SortBy
import feature.profile.domain.ProfileRepository
import feature.profile.domain.model.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

@OptIn(
    ExperimentalPagingApi::class,
    ExperimentalCoroutinesApi::class
)
@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val favoriteMovieDao: FavoriteMovieDao,
    private val watchlistMovieDao: WatchlistMovieDao,
    private val ratedMovieDao: RatedMovieDao,
    private val favoriteTvShowDao: FavoriteTvShowDao,
    private val watchlistTvShowDao: WatchlistTvShowDao,
    private val ratedTvShowDao: RatedTvShowDao,
    private val languagePreference: LanguagePreference,
) : ProfileRepository {
    private val favoriteMoviesRefreshTrigger = MutableStateFlow(0)
    private val watchlistMoviesRefreshTrigger = MutableStateFlow(0)
    private val ratedMoviesRefreshTrigger = MutableStateFlow(0)
    private val favoriteTvShowsRefreshTrigger = MutableStateFlow(0)
    private val watchlistTvShowsRefreshTrigger = MutableStateFlow(0)
    private val ratedTvShowsRefreshTrigger = MutableStateFlow(0)

    override fun getFavoriteMovies(sortBy: SortBy.CreatedAt): Flow<PagingData<Movie>> {
        return favoriteMoviesRefreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = FavoriteMoviesRemoteMediator(
                    getMovies = { page ->
                        accountService.getFavoriteMovies(
                            page,
                            sessionManager.requireSessionId(),
                            languagePreference.getLanguageTag(),
                            sortBy.toDto().value
                        )
                    },
                    favoriteMovieDao
                ),
                pagingSourceFactory = { favoriteMovieDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        imagePath = entity.posterPath,
                        voteAverage = entity.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }


    override fun getWatchlistMovies(sortBy: SortBy.CreatedAt): Flow<PagingData<Movie>> {
        return watchlistMoviesRefreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = WatchlistMoviesRemoteMediator(
                    getMovies = { page ->
                        accountService.getWatchlistMovies(
                            page,
                            sessionManager.requireSessionId(),
                            languagePreference.getLanguageTag(),
                            sortBy.toDto().value
                        )
                    },
                    watchlistMovieDao
                ),
                pagingSourceFactory = { watchlistMovieDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        imagePath = entity.posterPath,
                        voteAverage = entity.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    override fun getRatedMovies(sortBy: SortBy.CreatedAt): Flow<PagingData<Movie>> {
        return ratedMoviesRefreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = RatedMoviesRemoteMediator(
                    getMovies = { page ->
                        accountService.getRatedMovies(
                            page,
                            sessionManager.requireSessionId(),
                            languagePreference.getLanguageTag(),
                            sortBy.toDto().value
                        )
                    },
                    ratedMovieDao
                ),
                pagingSourceFactory = { ratedMovieDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        imagePath = entity.posterPath,
                        voteAverage = entity.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    override fun getFavoriteTvShows(sortBy: SortBy.CreatedAt): Flow<PagingData<Movie>> {
        return favoriteTvShowsRefreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = FavoriteTvShowsRemoteMediator(
                    getTvShows = { page ->
                        accountService.getFavoriteTvShows(
                            page,
                            sessionManager.requireSessionId(),
                            languagePreference.getLanguageTag(),
                            sortBy.toDto().value
                        )
                    },
                    favoriteTvShowDao
                ),
                pagingSourceFactory = { favoriteTvShowDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        imagePath = entity.posterPath,
                        voteAverage = entity.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    override fun getWatchlistTvShows(sortBy: SortBy.CreatedAt): Flow<PagingData<Movie>> {
        return watchlistTvShowsRefreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = WatchlistTvShowsRemoteMediator(
                    getTvShows = { page ->
                        accountService.getWatchlistTvShows(
                            page,
                            sessionManager.requireSessionId(),
                            languagePreference.getLanguageTag(),
                            sortBy.toDto().value
                        )
                    },
                    watchlistTvShowDao
                ),
                pagingSourceFactory = { watchlistTvShowDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        imagePath = entity.posterPath,
                        voteAverage = entity.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    override fun getRatedTvShows(sortBy: SortBy.CreatedAt): Flow<PagingData<Movie>> {
        return ratedTvShowsRefreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = RatedTvShowsRemoteMediator(
                    getTvShows = { page ->
                        accountService.getRatedTvShows(
                            page,
                            sessionManager.requireSessionId(),
                            languagePreference.getLanguageTag(),
                            sortBy.toDto().value
                        )
                    },
                    ratedTvShowDao
                ),
                pagingSourceFactory = { ratedTvShowDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        imagePath = entity.posterPath,
                        voteAverage = entity.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    override fun updateRatedMovies() {
        ratedMoviesRefreshTrigger.update { it + 1 }
    }

    override fun updateWatchlistMovies() {
        watchlistMoviesRefreshTrigger.update { it + 1 }
    }

    override fun updateFavoriteMovies() {
        favoriteMoviesRefreshTrigger.update { it + 1 }
    }

    override fun updateRatedTvShows() {
        ratedTvShowsRefreshTrigger.update { it + 1 }
    }

    override fun updateWatchlistTvShows() {
        watchlistTvShowsRefreshTrigger.update { it + 1 }
    }

    override fun updateFavoriteTvShows() {
        favoriteTvShowsRefreshTrigger.update { it + 1 }
    }

    override fun updateLanguageRelatedData() {
        ratedMoviesRefreshTrigger.update { it + 1 }
        watchlistMoviesRefreshTrigger.update { it + 1 }
        favoriteMoviesRefreshTrigger.update { it + 1 }
        ratedTvShowsRefreshTrigger.update { it + 1 }
        watchlistTvShowsRefreshTrigger.update { it + 1 }
        favoriteTvShowsRefreshTrigger.update { it + 1 }
    }
}
