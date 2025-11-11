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
import core.domain.account.SortBy
import core.domain.profile.ProfileFavoriteMoviesPagingInvalidator
import core.domain.profile.ProfileRatedMoviesPagingInvalidator
import core.domain.profile.ProfileWatchlistMoviesPagingInvalidator
import feature.profile.domain.Movie
import feature.profile.domain.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import libraries.image.TmdbImage

@OptIn(ExperimentalPagingApi::class)
class ProfileRepositoryImpl(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val favoriteMovieDao: FavoriteMovieDao,
    private val watchlistMovieDao: WatchlistMovieDao,
    private val ratedMovieDao: RatedMovieDao,
    private val languagePreference: LanguagePreference,
) : ProfileRepository, ProfileFavoriteMoviesPagingInvalidator,
    ProfileWatchlistMoviesPagingInvalidator,
    ProfileRatedMoviesPagingInvalidator {
    private val refreshFavoriteMoviesTrigger = MutableStateFlow(0)
    private val refreshWatchlistMoviesTrigger = MutableStateFlow(0)
    private val refreshRatedMoviesTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavoriteMovies(sortBy: SortBy.CreatedAt): Flow<PagingData<Movie>> {
        return refreshFavoriteMoviesTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = FavoriteMoviesRemoteMediator(
                    getMovies = { page ->
                        accountService.getFavoriteMovies(
                            page,
                            sessionManager.requireSessionId(),
                            languagePreference.getLanguageTag(),
                            sortBy.toDto()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getWatchlistMovies(sortBy: SortBy.CreatedAt): Flow<PagingData<Movie>> {
        return refreshWatchlistMoviesTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = WatchlistMoviesRemoteMediator(
                    getMovies = { page ->
                        accountService.getWatchlistMovies(
                            page,
                            sessionManager.requireSessionId(),
                            languagePreference.getLanguageTag(),
                            sortBy.toDto()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getRatedMovies(sortBy: SortBy.CreatedAt): Flow<PagingData<Movie>> {
        return refreshRatedMoviesTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = RatedMoviesRemoteMediator(
                    getMovies = { page ->
                        accountService.getRatedMovies(
                            page,
                            sessionManager.requireSessionId(),
                            languagePreference.getLanguageTag(),
                            sortBy.toDto()
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

    override fun invalidateRatedMovies() {
        refreshRatedMoviesTrigger.update { it + 1 }
    }

    override fun invalidateWatchlistMovies() {
        refreshWatchlistMoviesTrigger.update { it + 1 }
    }

    override fun invalidateFavoriteMovies() {
        refreshFavoriteMoviesTrigger.update { it + 1 }
    }
}