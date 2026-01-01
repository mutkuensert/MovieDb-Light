package core.data.account

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.data.SessionManager
import core.data.account.model.AccountDetailsResponse
import core.data.account.model.FavoriteMovieRequest
import core.data.account.model.WatchlistMovieRequest
import core.data.account.model.toDto
import core.data.common.model.MovieDto
import core.data.network.toFailure
import core.database.LanguagePreference
import core.database.account.FavoriteMovieDao
import core.database.account.FavoriteTvShowDao
import core.database.account.RatedMovieDao
import core.database.account.WatchlistMovieDao
import core.database.account.model.FavoriteMovieIdEntity
import core.database.account.model.WatchlistMovieIdEntity
import core.database.user.UserDetails
import core.database.user.UserManager
import core.domain.AuthFailure
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.account.SortBy
import core.domain.account.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import libraries.stringresource.StrResource
import moviedblight.core.data.R

class AccountRepositoryImpl(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val userManager: UserManager,
    private val favoriteMovieDao: FavoriteMovieDao,
    private val watchlistMovieDao: WatchlistMovieDao,
    private val ratedMovieDao: RatedMovieDao,
    private val favoriteTvShowDao: FavoriteTvShowDao,
    private val languagePreference: LanguagePreference,
    private val strResource: StrResource,
) : AccountRepository {

    override suspend fun fetchAccountDetails(): Result<User, Failure> {
        val user = userManager.getUser()?.toUser()
        if (user != null) {
            return Ok(user)
        }
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

            favoriteMovieDao.insertIds(
                *favoriteMovies
                    .map { it.toFavoriteMovieIdEntity() }
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

            watchlistMovieDao.insertIds(
                *watchlistMovies
                    .map { it.toWatchlistMovieIdEntity() }
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
            val somethingIsWrongMessage = strResource.get(R.string.something_is_wrong)
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
                    favoriteMovieDao.deleteIds(FavoriteMovieIdEntity(movieId))
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
                    watchlistMovieDao.deleteIds(WatchlistMovieIdEntity(movieId))
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
            ratedMovieDao.clearAllMovies()
            favoriteTvShowDao.clearAll()
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