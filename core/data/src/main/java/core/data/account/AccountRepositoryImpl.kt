package core.data.account

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onErr
import com.github.michaelbull.result.onOk
import core.data.SessionManager
import core.data.account.model.AccountDetailsResponse
import core.data.account.model.FavoriteMovieRequest
import core.data.account.model.FavoriteTvShowDto
import core.data.account.model.WatchlistMovieRequest
import core.data.account.model.WatchlistTvShowRequest
import core.data.account.model.toDto
import core.data.common.model.MovieDto
import core.data.common.model.TvShowDto
import core.data.network.toFailure
import core.database.LanguagePreference
import core.database.account.FavoriteMovieDao
import core.database.account.FavoriteTvShowDao
import core.database.account.RatedMovieDao
import core.database.account.RatedTvShowDao
import core.database.account.WatchlistMovieDao
import core.database.account.WatchlistTvShowDao
import core.database.account.model.FavoriteMovieIdEntity
import core.database.account.model.FavoriteTvShowIdEntity
import core.database.account.model.WatchlistMovieIdEntity
import core.database.account.model.WatchlistTvShowIdEntity
import core.database.user.UserManager
import core.domain.AuthFailure
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.account.SortBy
import core.domain.account.User
import filmcan.core.data.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utils.stringresource.StringResource

class AccountRepositoryImpl(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val userManager: UserManager,
    private val favoriteMovieDao: FavoriteMovieDao,
    private val watchlistMovieDao: WatchlistMovieDao,
    private val ratedMovieDao: RatedMovieDao,
    private val favoriteTvShowDao: FavoriteTvShowDao,
    private val watchlistTvShowDao: WatchlistTvShowDao,
    private val ratedTvShowDao: RatedTvShowDao,
    private val languagePreference: LanguagePreference,
    private val stringResource: StringResource,
) : AccountRepository {

    override suspend fun getAccountDetails(): Result<User, Failure> {
        val sessionId = sessionManager.requireSessionId()
        return accountService.getAccountDetails(sessionId)
            .mapBoth(success = { response ->
                userManager.setCurrentUser(
                    response.id,
                    response.name,
                    response.username,
                    response.avatar.tmdb.avatarPath,
                    response.includeAdult,
                )
                Ok(response.toUser())
            }, failure = { networkError ->
                Err(networkError.toFailure())
            })
    }

    override suspend fun fetchFavoriteMovies(sortBy: SortBy.CreatedAt) {
        withContext(Dispatchers.IO) {
            favoriteMovieDao.clearAllIds()
            val favoriteMovies = mutableListOf<MovieDto>()

            var endPage = 2
            var page = 1
            while (page in 0..endPage) {
                accountService.getFavoriteMovies(
                    page,
                    sessionManager.requireSessionId(),
                    languagePreference.getLanguageTag(),
                    sortBy.toDto().value
                ).onOk { response ->
                    endPage = response.totalPages

                    if (response.results.isNotEmpty()) {
                        favoriteMovies.addAll(response.results)
                    }
                }.onErr { break }
                page++
            }

            favoriteMovieDao.insertIds(
                *favoriteMovies
                    .map { it.toFavoriteMovieIdEntity() }
                    .toTypedArray()
            )
        }
    }

    override suspend fun fetchFavoriteTvShows(sortBy: SortBy.CreatedAt) {
        withContext(Dispatchers.IO) {
            favoriteTvShowDao.clearAllIds()
            val favoriteTvShows = mutableListOf<TvShowDto>()

            var endPage = 2
            var page = 1
            while (page in 0..endPage) {
                accountService.getFavoriteTvShows(
                    page,
                    sessionManager.requireSessionId(),
                    languagePreference.getLanguageTag(),
                    sortBy.toDto().value
                ).onOk { response ->
                    endPage = response.totalPages

                    if (response.results.isNotEmpty()) {
                        favoriteTvShows.addAll(response.results)
                    }
                }.onErr { break }
                page++
            }

            favoriteTvShowDao.insertIds(
                *favoriteTvShows
                    .map { it.toFavoriteTvShowIdEntity() }
                    .toTypedArray()
            )
        }
    }

    override suspend fun fetchWatchlistMovies(sortBy: SortBy.CreatedAt) {
        withContext(Dispatchers.IO) {
            watchlistMovieDao.clearAllIds()
            val watchlistMovies = mutableListOf<MovieDto>()

            var endPage = 2
            var page = 1
            while (page in 0..endPage) {
                accountService.getWatchlistMovies(
                    page,
                    sessionManager.requireSessionId(),
                    languagePreference.getLanguageTag(),
                    sortBy.toDto().value

                ).onOk { response ->
                    endPage = response.totalPages

                    if (response.results.isNotEmpty()) {
                        watchlistMovies.addAll(response.results)
                    }
                }.onErr { break }
                page++
            }

            watchlistMovieDao.insertIds(
                *watchlistMovies
                    .map { it.toWatchlistMovieIdEntity() }
                    .toTypedArray()
            )
        }
    }

    override suspend fun fetchWatchlistTvShows(sortBy: SortBy.CreatedAt) {
        withContext(Dispatchers.IO) {
            watchlistTvShowDao.clearAllIds()
            val watchlistTvShows = mutableListOf<TvShowDto>()

            var endPage = 2
            var page = 1
            while (page in 0..endPage) {
                accountService.getWatchlistTvShows(
                    page,
                    sessionManager.requireSessionId(),
                    languagePreference.getLanguageTag(),
                    sortBy.toDto().value

                ).onOk { response ->
                    endPage = response.totalPages

                    if (response.results.isNotEmpty()) {
                        watchlistTvShows.addAll(response.results)
                    }
                }.onErr { break }
                page++
            }

            watchlistTvShowDao.insertIds(
                *watchlistTvShows
                    .map { it.toWatchlistTvShowIdEntity() }
                    .toTypedArray()
            )
        }
    }

    override suspend fun syncMovieFavoriteStatus(
        movieId: Int,
        isFavorite: Boolean,
    ): Result<Unit, Failure> {
        return withContext(Dispatchers.IO) {
            if (isFavorite) {
                favoriteMovieDao.insertIds(FavoriteMovieIdEntity(movieId))
            } else {
                favoriteMovieDao.deleteIds(FavoriteMovieIdEntity(movieId))
            }
            val somethingIsWrongMessage = stringResource.get(R.string.something_is_wrong)
            val sessionId = sessionManager.getSessionId()
                ?: return@withContext Err(AuthFailure(somethingIsWrongMessage))

            return@withContext accountService.postFavoriteMovie(
                FavoriteMovieRequest(
                    favorite = isFavorite,
                    mediaId = movieId
                ),
                sessionId = sessionId
            ).mapBoth(
                success = {
                    Ok(Unit)
                },
                failure = { networkError ->
                    if (isFavorite) {
                        favoriteMovieDao.deleteIds(FavoriteMovieIdEntity(movieId))
                    } else {
                        favoriteMovieDao.insertIds(FavoriteMovieIdEntity(movieId))
                    }

                    Err(networkError.toFailure())
                }
            )
        }
    }

    override suspend fun syncMovieWatchlistStatus(
        movieId: Int,
        inWatchlist: Boolean,
    ): Result<Unit, Failure> {
        return withContext(Dispatchers.IO) {
            if (inWatchlist) {
                watchlistMovieDao.insertIds(WatchlistMovieIdEntity(movieId))
            } else {
                watchlistMovieDao.deleteIds(WatchlistMovieIdEntity(movieId))
            }

            return@withContext accountService.postWatchlistMovie(
                WatchlistMovieRequest(
                    watchlist = inWatchlist,
                    mediaId = movieId
                ),
                sessionId = sessionManager.requireSessionId()
            ).mapBoth(
                success = {
                    Ok(Unit)
                },
                failure = { networkError ->
                    if (inWatchlist) {
                        watchlistMovieDao.deleteIds(WatchlistMovieIdEntity(movieId))
                    } else {
                        watchlistMovieDao.insertIds(WatchlistMovieIdEntity(movieId))
                    }
                    Err(networkError.toFailure())
                }
            )
        }
    }

    override suspend fun syncTvShowFavoriteStatus(
        tvShowId: Int,
        isFavorite: Boolean,
    ): Result<Unit, Failure> {
        return withContext(Dispatchers.IO) {
            if (isFavorite) {
                favoriteTvShowDao.insertIds(FavoriteTvShowIdEntity(tvShowId))
            } else {
                favoriteTvShowDao.deleteIds(FavoriteTvShowIdEntity(tvShowId))
            }
            val sessionId = sessionManager.getSessionId()
                ?: return@withContext Err(AuthFailure(stringResource.get(R.string.something_is_wrong)))

            return@withContext accountService.postFavoriteTvShow(
                FavoriteTvShowDto(
                    favorite = isFavorite,
                    mediaId = tvShowId
                ),
                sessionId = sessionId
            ).mapBoth(
                success = { Ok(Unit) },
                failure = { networkError ->
                    if (isFavorite) {
                        favoriteTvShowDao.deleteIds(FavoriteTvShowIdEntity(tvShowId))
                    } else {
                        favoriteTvShowDao.insertIds(FavoriteTvShowIdEntity(tvShowId))
                    }
                    Err(networkError.toFailure())
                }
            )
        }
    }

    override suspend fun syncTvShowWatchlistStatus(
        tvShowId: Int,
        inWatchlist: Boolean,
    ): Result<Unit, Failure> {
        return withContext(Dispatchers.IO) {
            if (inWatchlist) {
                watchlistTvShowDao.insertIds(WatchlistTvShowIdEntity(tvShowId))
            } else {
                watchlistTvShowDao.deleteIds(WatchlistTvShowIdEntity(tvShowId))
            }

            return@withContext accountService.postWatchlistTvShow(
                WatchlistTvShowRequest(
                    watchlist = inWatchlist,
                    mediaId = tvShowId
                ),
                sessionId = sessionManager.requireSessionId()
            ).mapBoth(
                success = { Ok(Unit) },
                failure = { networkError ->
                    if (inWatchlist) {
                        watchlistTvShowDao.deleteIds(WatchlistTvShowIdEntity(tvShowId))
                    } else {
                        watchlistTvShowDao.insertIds(WatchlistTvShowIdEntity(tvShowId))
                    }
                    Err(networkError.toFailure())
                }
            )
        }
    }

    override suspend fun clearUserRelatedData() {
        withContext(Dispatchers.IO) {
            favoriteMovieDao.clearAllIds()
            favoriteMovieDao.clearAllMovies()
            watchlistMovieDao.clearAllIds()
            watchlistMovieDao.clearAllMovies()
            ratedMovieDao.clearAll()
            favoriteTvShowDao.clearAllTvShows()
            favoriteTvShowDao.clearAllIds()
            watchlistTvShowDao.clearAllTvShows()
            watchlistTvShowDao.clearAllIds()
            ratedTvShowDao.clearAll()
            userManager.removeCurrentUser()
        }
    }
}

private fun MovieDto.toFavoriteMovieIdEntity(): FavoriteMovieIdEntity {
    return FavoriteMovieIdEntity(id)
}

private fun MovieDto.toWatchlistMovieIdEntity(): WatchlistMovieIdEntity {
    return WatchlistMovieIdEntity(id)
}

private fun TvShowDto.toFavoriteTvShowIdEntity(): FavoriteTvShowIdEntity {
    return FavoriteTvShowIdEntity(id)
}

private fun TvShowDto.toWatchlistTvShowIdEntity(): WatchlistTvShowIdEntity {
    return WatchlistTvShowIdEntity(id)
}

private fun AccountDetailsResponse.toUser(): User {
    return User(
        id,
        name,
        username,
        avatar.tmdb.avatarPath,
        includeAdult
    )
}
