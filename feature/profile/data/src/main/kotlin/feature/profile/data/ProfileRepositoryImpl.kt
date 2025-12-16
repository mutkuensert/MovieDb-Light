package feature.profile.data

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
import core.database.account.RatedMovieDao
import core.database.account.WatchlistMovieDao
import core.domain.AppContentLanguageChangeListener
import core.domain.account.SortBy
import core.domain.profile.FavoriteMoviesRefresher
import core.domain.profile.RatedMoviesRefresher
import core.domain.profile.WatchlistMoviesRefresher
import feature.profile.domain.Movie
import feature.profile.domain.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import libraries.image.TmdbImage

@OptIn(
    ExperimentalPagingApi::class,
    ExperimentalCoroutinesApi::class
)
class ProfileRepositoryImpl(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val favoriteMovieDao: FavoriteMovieDao,
    private val watchlistMovieDao: WatchlistMovieDao,
    private val ratedMovieDao: RatedMovieDao,
    private val languagePreference: LanguagePreference,
) : ProfileRepository, FavoriteMoviesRefresher,
    WatchlistMoviesRefresher,
    RatedMoviesRefresher, AppContentLanguageChangeListener {
    private val favoriteMoviesRefreshTrigger = MutableStateFlow(0)
    private val watchlistMoviesRefreshTrigger = MutableStateFlow(0)
    private val ratedMoviesRefreshTrigger = MutableStateFlow(0)

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
                        imageUrl = entity.posterPath?.let { TmdbImage.Poster(it).w780Url },
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
                        imageUrl = entity.posterPath?.let { TmdbImage.Poster(it).w780Url },
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
                        imageUrl = entity.posterPath?.let { TmdbImage.Poster(it).w780Url },
                        voteAverage = entity.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    override fun refreshRatedMovies() {
        ratedMoviesRefreshTrigger.update { it + 1 }
    }

    override fun refreshWatchlistMovies() {
        watchlistMoviesRefreshTrigger.update { it + 1 }
    }

    override fun refreshFavoriteMovies() {
        favoriteMoviesRefreshTrigger.update { it + 1 }
    }

    override fun onAppContentLanguageChanged() {
        ratedMoviesRefreshTrigger.update { it + 1 }
        watchlistMoviesRefreshTrigger.update { it + 1 }
        favoriteMoviesRefreshTrigger.update { it + 1 }
    }
}