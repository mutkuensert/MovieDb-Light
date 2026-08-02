package core.data.account

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import core.data.SessionManager
import core.data.account.model.AccountDetailsResponse
import core.data.account.model.FavoriteMovieRequest
import core.data.account.model.FavoriteTvShowDto
import core.data.account.model.WatchlistMovieRequest
import core.data.account.model.WatchlistTvShowRequest
import core.data.auth.LogoutTrigger
import core.data.network.toFailure
import core.database.user.UserManager
import core.domain.AuthFailure
import core.domain.Failure
import core.domain.account.AccountRepository
import core.domain.account.User
import filmcan.core.data.R
import utils.stringresource.StringResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
    private val userManager: UserManager,
    private val stringResource: StringResource,
    private val logoutTrigger: LogoutTrigger,
) : AccountRepository {

    override suspend fun getAccountDetails(): Result<User, Failure> {
        val sessionId = sessionManager.getSessionId()
        if (sessionId == null) {
            logoutTrigger.triggerLogout()
            return createLoggedOutFailure()
        }

        return accountService.getAccountDetails(sessionId).mapBoth(
            success = { response ->
                userManager.setCurrentUser(
                    response.id,
                    response.name,
                    response.username,
                    response.avatar.tmdb.avatarPath,
                    response.includeAdult,
                )
                Ok(response.toUser())
            },
            failure = { networkError -> Err(networkError.toFailure()) },
        )
    }

    override suspend fun syncMovieFavoriteStatus(
        movieId: Int,
        isFavorite: Boolean,
    ): Result<Unit, Failure> {
        val sessionId = sessionManager.getSessionId() ?: return createLoggedOutFailure()
        return accountService.postFavoriteMovie(
            FavoriteMovieRequest(favorite = isFavorite, mediaId = movieId),
            sessionId = sessionId,
        ).mapBoth(
            success = { Ok(Unit) },
            failure = { networkError -> Err(networkError.toFailure()) },
        )
    }

    override suspend fun syncMovieWatchlistStatus(
        movieId: Int,
        inWatchlist: Boolean,
    ): Result<Unit, Failure> {
        val sessionId = sessionManager.getSessionId()
        if (sessionId == null) {
            logoutTrigger.triggerLogout()
            return createLoggedOutFailure()
        }

        return accountService.postWatchlistMovie(
            WatchlistMovieRequest(watchlist = inWatchlist, mediaId = movieId),
            sessionId = sessionId,
        ).mapBoth(
            success = { Ok(Unit) },
            failure = { networkError -> Err(networkError.toFailure()) },
        )
    }

    override suspend fun syncTvShowFavoriteStatus(
        tvShowId: Int,
        isFavorite: Boolean,
    ): Result<Unit, Failure> {
        val sessionId = sessionManager.getSessionId() ?: return createLoggedOutFailure()
        return accountService.postFavoriteTvShow(
            FavoriteTvShowDto(favorite = isFavorite, mediaId = tvShowId),
            sessionId = sessionId,
        ).mapBoth(
            success = { Ok(Unit) },
            failure = { networkError -> Err(networkError.toFailure()) },
        )
    }

    override suspend fun syncTvShowWatchlistStatus(
        tvShowId: Int,
        inWatchlist: Boolean,
    ): Result<Unit, Failure> {
        val sessionId = sessionManager.getSessionId()
        if (sessionId == null) {
            logoutTrigger.triggerLogout()
            return createLoggedOutFailure()
        }

        return accountService.postWatchlistTvShow(
            WatchlistTvShowRequest(watchlist = inWatchlist, mediaId = tvShowId),
            sessionId = sessionId,
        ).mapBoth(
            success = { Ok(Unit) },
            failure = { networkError -> Err(networkError.toFailure()) },
        )
    }

    override suspend fun clearUserRelatedData() {
        userManager.removeCurrentUser()
    }

    private fun createLoggedOutFailure(): Result<Nothing, Failure> {
        return Err(AuthFailure(stringResource.get(R.string.logged_out_unknown_reason)))
    }
}

private fun AccountDetailsResponse.toUser(): User {
    return User(
        id,
        name,
        username,
        avatar.tmdb.avatarPath,
        includeAdult,
    )
}
