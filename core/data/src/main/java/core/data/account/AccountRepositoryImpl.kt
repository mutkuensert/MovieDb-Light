package core.data.account

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.data.SessionManager
import core.data.model.common.MovieDto
import core.database.account.AccountDao
import core.database.account.model.FavoriteMovieEntity
import core.database.account.model.WatchlistMovieEntity
import core.database.user.UserDetails
import core.database.user.UserManager
import core.domain.AccountRepository
import core.domain.AuthStateListener
import core.domain.ErrorMessage
import core.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val userManager: UserManager,
    private val accountDao: AccountDao,
) : AccountRepository, AuthStateListener {

    override suspend fun fetchAccountDetails(): Result<User, ErrorMessage> {
        val user = userManager.getUser()?.toUser()
        if (user != null) {
            return Ok(user)
        }
        val sessionId = requireNotNull(sessionManager.getSessionId()) {
            "Session id can't be null here."
        }
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
            }, failure = {
                Err(it.message)
            })
    }

    override suspend fun fetchFavoriteMovies() {
        withContext(Dispatchers.IO) {
            accountDao.clearAllFavoriteMovies()
            val favoriteMovies = mutableListOf<MovieDto>()

            var endPage = 2
            var page = 1
            while (page in 0..endPage) {
                accountService.getFavoriteMovies(
                    page = page,
                    sessionId = sessionManager.getSessionId()!!
                ).onSuccess { response ->
                    endPage = response.totalPages

                    if (response.results.isNotEmpty()) {
                        favoriteMovies.addAll(response.results)
                    }
                }.onFailure {
                    break
                }
                page++
            }

            accountDao.insertFavoriteMovies(
                *favoriteMovies
                    .map(::mapToFavoriteMovieEntity)
                    .toTypedArray()
            )
        }
    }

    override suspend fun fetchWatchlistMovies() {
        withContext(Dispatchers.IO) {
            accountDao.clearAllWatchlistMovies()
            val watchlistMovies = mutableListOf<MovieDto>()

            var endPage = 2
            var page = 1
            while (page in 0..endPage) {
                accountService.getWatchlistMovies(
                    page = page,
                    sessionId = sessionManager.requireSessionId()
                ).onSuccess { response ->
                    endPage = response.totalPages

                    if (response.results.isNotEmpty()) {
                        watchlistMovies.addAll(response.results)
                    }
                }.onFailure {
                    break
                }
                page++
            }

            accountDao.insertWatchlistMovies(
                *watchlistMovies
                    .map(::mapToWatchlistMovieEntity)
                    .toTypedArray()
            )
        }
    }

    override suspend fun syncMovieFavoriteStatus(
        movieId: Int,
        isFavorite: Boolean,
    ): Result<Unit, ErrorMessage> {
        return withContext(Dispatchers.IO) {
            if (isFavorite) {
                accountDao.insertFavoriteMovies(FavoriteMovieEntity(movieId))
            } else {
                accountDao.deleteFavoriteMovies(FavoriteMovieEntity(movieId))
            }

            return@withContext accountService.postFavoriteMovie(
                FavoriteMovieRequest(
                    favorite = isFavorite,
                    mediaId = movieId
                ),
                sessionId = sessionManager.requireSessionId()
            ).mapBoth(
                success = {
                    Ok(Unit)
                },
                failure = {
                    accountDao.deleteFavoriteMovies(FavoriteMovieEntity(movieId))
                    Err(it.message)
                }
            )
        }
    }

    override suspend fun syncMovieWatchlistStatus(
        movieId: Int,
        inWatchlist: Boolean,
    ): Result<Unit, ErrorMessage> {
        return withContext(Dispatchers.IO) {
            if (inWatchlist) {
                accountDao.insertWatchlistMovies(WatchlistMovieEntity(movieId))
            } else {
                accountDao.deleteWatchlistMovies(WatchlistMovieEntity(movieId))
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
                failure = {
                    accountDao.deleteWatchlistMovies(WatchlistMovieEntity(movieId))
                    Err(it.message)
                }
            )
        }
    }

    override suspend fun onUnauthorized() {
        withContext(Dispatchers.IO) {
            accountDao.clearAllFavoriteMovies()
            accountDao.clearAllFavoriteTvShows()
            userManager.removeCurrentUser()
        }
    }
}

private fun mapToFavoriteMovieEntity(
    dto: MovieDto
): FavoriteMovieEntity {
    return FavoriteMovieEntity(id = dto.id)
}

private fun mapToWatchlistMovieEntity(
    dto: MovieDto
): WatchlistMovieEntity {
    return WatchlistMovieEntity(id = dto.id)
}

private fun UserDetails.toUser(): User {
    return User(
        id,
        name,
        userName,
        profilePicturePath,
        includeAdult
    )
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