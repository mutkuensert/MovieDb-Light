package feature.profile.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import core.data.SessionManager
import core.data.account.AccountService
import core.data.util.withDecimals
import feature.profile.domain.Movie
import feature.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import libraries.image.TmdbImage

class ProfileRepositoryImpl(
    private val accountService: AccountService,
    private val sessionManager: SessionManager,
) : ProfileRepository {

    override fun getFavoriteMovies(): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            GenericMoviesPagingSource {
                accountService.getFavoriteMovies(it, sessionManager.requireSessionId())
            }
        }.flow.map { pagingData ->
            pagingData.map {
                Movie(
                    it.id,
                    it.title,
                    it.posterPath?.let { path -> TmdbImage.Poster(path).w780Url },
                    it.voteAverage?.withDecimals(1)
                )
            }
        }
    }

    override fun getWatchlistMovies(): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            GenericMoviesPagingSource {
                accountService.getWatchlistMovies(it, sessionManager.requireSessionId())
            }
        }.flow.map { pagingData ->
            pagingData.map {
                Movie(
                    it.id,
                    it.title,
                    it.posterPath?.let { path -> TmdbImage.Poster(path).w780Url },
                    it.voteAverage?.withDecimals(1)
                )
            }
        }
    }

    override fun getRatedMovies(): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            GenericMoviesPagingSource {
                accountService.getRatedMovies(it, sessionManager.requireSessionId())
            }
        }.flow.map { pagingData ->
            pagingData.map {
                Movie(
                    it.id,
                    it.title,
                    it.posterPath?.let { path -> TmdbImage.Poster(path).w780Url },
                    it.voteAverage?.withDecimals(1)
                )
            }
        }
    }
}