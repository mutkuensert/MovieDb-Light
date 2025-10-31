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
import core.domain.ErrorMessage
import core.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val userManager: UserManager,
    private val accountDao: AccountDao,
) : AccountRepository {

    override suspend fun fetchAccountDetails(): Result<Unit, ErrorMessage> {
        return accountService.getAccountDetails(sessionManager.getSessionId()!!)
            .mapBoth(success = {
                userManager.setCurrentUser(
                    profilePicturePath = it.avatar.tmdb.avatarPath,
                    id = it.id,
                    includeAdult = it.includeAdult,
                    name = it.name,
                    userName = it.username
                )
                Ok(Unit)
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

    override fun getUser(): User {
        val userDetails = requireNotNull(userManager.getUser()) {
            "If logged in, user info should not be null"
        }
        return User(
            id = userDetails.id,
            name = userDetails.name,
            userName = userDetails.userName,
            profilePicturePath = userDetails.profilePicturePath,
            includeAdult = userDetails.includeAdult
        )
    }
}

private fun mapToFavoriteMovieEntity(
    dto: FavoriteMoviesResultDto
): FavoriteMovieEntity {
    return FavoriteMovieEntity(id = dto.id)
}
