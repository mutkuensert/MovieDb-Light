package feature.profile.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.michaelbull.result.Err
import core.data.SessionManager
import core.data.account.AccountService
import core.data.account.model.toDto
import core.data.auth.LogoutTrigger
import core.data.common.model.MoviesResponse
import core.data.common.model.TvShowsResponse
import core.data.network.NetworkError
import core.data.network.NetworkResult
import core.data.paging.MoviesPagingSource
import core.data.paging.TvShowsPagingSource
import core.data.util.withDecimals
import core.domain.account.SortBy
import feature.profile.domain.ProfileRepository
import feature.profile.domain.model.Production
import filmcan.core.data.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import utils.stringresource.StringResource
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val logoutTrigger: LogoutTrigger,
    private val stringResource: StringResource,
) : ProfileRepository {
    private val favoriteMoviesRefreshTrigger = MutableStateFlow(0)
    private val watchlistMoviesRefreshTrigger = MutableStateFlow(0)
    private val ratedMoviesRefreshTrigger = MutableStateFlow(0)
    private val favoriteTvShowsRefreshTrigger = MutableStateFlow(0)
    private val watchlistTvShowsRefreshTrigger = MutableStateFlow(0)
    private val ratedTvShowsRefreshTrigger = MutableStateFlow(0)

    override fun getFavoriteMovies(sortBy: SortBy.CreatedAt): Flow<PagingData<Production>> {
        return movieFeed(favoriteMoviesRefreshTrigger) { page, sessionId ->
            accountService.getFavoriteMovies(page, sessionId, sortBy.toDto().value)
        }
    }

    override fun getWatchlistMovies(sortBy: SortBy.CreatedAt): Flow<PagingData<Production>> {
        return movieFeed(watchlistMoviesRefreshTrigger) { page, sessionId ->
            accountService.getWatchlistMovies(page, sessionId, sortBy.toDto().value)
        }
    }

    override fun getRatedMovies(sortBy: SortBy.CreatedAt): Flow<PagingData<Production>> {
        return movieFeed(ratedMoviesRefreshTrigger) { page, sessionId ->
            accountService.getRatedMovies(page, sessionId, sortBy.toDto().value)
        }
    }

    override fun getFavoriteTvShows(sortBy: SortBy.CreatedAt): Flow<PagingData<Production>> {
        return tvShowFeed(favoriteTvShowsRefreshTrigger) { page, sessionId ->
            accountService.getFavoriteTvShows(page, sessionId, sortBy.toDto().value)
        }
    }

    override fun getWatchlistTvShows(sortBy: SortBy.CreatedAt): Flow<PagingData<Production>> {
        return tvShowFeed(watchlistTvShowsRefreshTrigger) { page, sessionId ->
            accountService.getWatchlistTvShows(page, sessionId, sortBy.toDto().value)
        }
    }

    override fun getRatedTvShows(sortBy: SortBy.CreatedAt): Flow<PagingData<Production>> {
        return tvShowFeed(ratedTvShowsRefreshTrigger) { page, sessionId ->
            accountService.getRatedTvShows(page, sessionId, sortBy.toDto().value)
        }
    }

    private fun movieFeed(
        refreshTrigger: Flow<Int>,
        getMovies: suspend (page: Int, sessionId: String) -> NetworkResult<MoviesResponse>,
    ): Flow<PagingData<Production>> {
        return refreshTrigger.flatMapLatest {
            Pager(PagingConfig(pageSize = 20)) {
                MoviesPagingSource { page ->
                    withSession { sessionId -> getMovies(page, sessionId) }
                }
            }.flow.map { pagingData ->
                pagingData.map { movie ->
                    Production(
                        id = movie.id,
                        title = movie.title,
                        imagePath = movie.posterPath,
                        voteAverage = movie.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    private fun tvShowFeed(
        refreshTrigger: Flow<Int>,
        getTvShows: suspend (page: Int, sessionId: String) -> NetworkResult<TvShowsResponse>,
    ): Flow<PagingData<Production>> {
        return refreshTrigger.flatMapLatest {
            Pager(PagingConfig(pageSize = 20)) {
                TvShowsPagingSource { page ->
                    withSession { sessionId -> getTvShows(page, sessionId) }
                }
            }.flow.map { pagingData ->
                pagingData.map { tvShow ->
                    Production(
                        id = tvShow.id,
                        title = tvShow.name,
                        imagePath = tvShow.posterPath,
                        voteAverage = tvShow.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    private suspend fun <T> withSession(
        request: suspend (sessionId: String) -> NetworkResult<T>,
    ): NetworkResult<T> {
        val sessionId = sessionManager.getSessionId()
        if (sessionId != null) {
            return request(sessionId)
        }

        logoutTrigger.triggerLogout()
        return Err(
            NetworkError(
                httpCode = null,
                statusCode = null,
                message = stringResource.get(R.string.logged_out_unknown_reason),
            )
        )
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
