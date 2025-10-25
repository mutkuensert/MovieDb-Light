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
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieDao
import core.database.user.UserManager
import core.domain.AccountRepository
import core.domain.ErrorMessage
import core.libraries.AppScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountRepositoryImpl(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val userManager: UserManager,
    private val accountDao: AccountDao,
    private val popularMovieDao: PopularMovieDao,
    private val upcomingMovieDao: UpcomingMovieDao,
    private val topRatedMovieDao: TopRatedMovieDao,
    private val nowPlayingMovieDao: NowPlayingMovieDao,
    appScope: AppScope,
) : AccountRepository {

    init {
        appScope.launch {
            sessionManager.loggedIn.collectLatest { loggedIn ->
                if (loggedIn) {
                    fetchUserDetails().onSuccess {
                        fetchFavoriteMovies()
                    }.onFailure {
                        TODO("")
                    }
                }
            }
        }
    }

    override suspend fun fetchUserDetails(): Result<Unit, ErrorMessage> {
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

            updateFavoriteStatusOfMovieInDatabase(movieId, isFavorite)

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
                    updateFavoriteStatusOfMovieInDatabase(movieId, !isFavorite)
                    Err(it.message)
                }
            )
        }
    }

    private suspend fun updateFavoriteStatusOfMovieInDatabase(movieId: Int, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            val topRatedMovie = topRatedMovieDao.get(movieId)
            topRatedMovie?.copy(isFavorite = isFavorite)
                ?.apply { primaryKey = topRatedMovie.primaryKey }
                ?.let {
                    topRatedMovieDao.update(it)
                }

            val popularMovie = popularMovieDao.get(movieId)
            popularMovie?.copy(isFavorite = isFavorite)
                ?.apply { primaryKey = popularMovie.primaryKey }
                ?.let {
                    popularMovieDao.update(it)
                }

            val nowPlayingMovie = nowPlayingMovieDao.get(movieId)
            nowPlayingMovie?.copy(isFavorite = isFavorite)
                ?.apply { primaryKey = nowPlayingMovie.primaryKey }
                ?.let {
                    nowPlayingMovieDao.update(it)
                }

            val upcomingMovie = upcomingMovieDao.get(movieId)
            upcomingMovie?.copy(isFavorite = isFavorite)
                ?.apply { primaryKey = upcomingMovie.primaryKey }
                ?.let {
                    upcomingMovieDao.update(it)
                }
        }
    }
}

private fun mapToFavoriteMovieEntity(
    dto: FavoriteMoviesResultDto
): FavoriteMovieEntity {
    return FavoriteMovieEntity(id = dto.id)
}
