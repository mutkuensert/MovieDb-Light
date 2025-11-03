package core.data.account

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.data.SessionManager
import core.database.account.AccountDao
import core.database.account.model.FavoriteMovieEntity
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
        val user = userManager.getUser()
        if (user != null) {
            return Ok(
                User(
                    user.id,
                    user.name,
                    user.userName,
                    user.profilePicturePath,
                    user.includeAdult
                )
            )
        }
        return accountService.getAccountDetails(sessionManager.getSessionId()!!)
            .mapBoth(success = { response ->
                userManager.setCurrentUser(
                    id = response.id,
                    name = response.name,
                    userName = response.username,
                    profilePicturePath = response.avatar.tmdb.avatarPath,
                    includeAdult = response.includeAdult,
                )
                Ok(
                    User(
                        response.id,
                        response.name,
                        response.username,
                        response.avatar.tmdb.avatarPath,
                        response.includeAdult
                    )
                )
            }, failure = {
                Err(it.message)
            })
    }

    override suspend fun fetchFavoriteMovies() {
        withContext(Dispatchers.IO) {
            accountDao.clearAllFavoriteMovies()
            val favoriteMovies = mutableListOf<FavoriteMoviesResultDto>()

            var endPage = 2
            var page = 1
            while (page in 0..endPage) {
                accountService.getFavoriteMovies(
                    page = page,
                    sessionId = sessionManager.getSessionId()!!
                ).onSuccess { response: FavoriteMoviesResponse ->
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

    override suspend fun syncMovieFavoriteStatus(
        isFavorite: Boolean,
        movieId: Int
    ): Result<Unit, ErrorMessage> {
        return withContext(Dispatchers.IO) {
            if (isFavorite) {
                accountDao.insertFavoriteMovies(FavoriteMovieEntity(movieId))
            } else {
                accountDao.deleteFavoriteMovies(FavoriteMovieEntity(movieId))
            }

            return@withContext accountService.postFavoriteMovie(
                FavoriteMovieDto(
                    favorite = isFavorite,
                    mediaId = movieId
                ),
                sessionId = sessionManager.getSessionId()!!
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

    override suspend fun onUnauthorized() {
        withContext(Dispatchers.IO) {
            accountDao.clearAllFavoriteMovies()
            accountDao.clearAllFavoriteTvShows()
        }
    }
}

private fun mapToFavoriteMovieEntity(
    dto: FavoriteMoviesResultDto
): FavoriteMovieEntity {
    return FavoriteMovieEntity(id = dto.id)
}
