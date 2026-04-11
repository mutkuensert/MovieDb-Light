package feature.tvshow.domain

import androidx.paging.PagingData
import com.github.michaelbull.result.Result
import core.domain.Failure
import core.domain.common.model.Provider
import feature.tvshow.domain.model.AccountStates
import feature.tvshow.domain.model.TvShow
import feature.tvshow.domain.model.TvShowDetails
import feature.tvshow.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {
    fun getPopularTvShows(countryCode: String? = null): Flow<PagingData<TvShow>>
    fun getTvShowsAiringToday(countryCode: String? = null): Flow<PagingData<TvShow>>
    fun getUpcomingTvShows(countryCode: String? = null): Flow<PagingData<TvShow>>
    fun getTopRatedTvShows(countryCode: String? = null): Flow<PagingData<TvShow>>
    fun getSimilarTvShows(tvShowId: Int): Flow<PagingData<TvShow>>
    suspend fun getTvShowDetails(tvShowId: Int): Result<TvShowDetails, Failure>
    suspend fun getCast(tvShowId: Int): Result<List<Person>, Failure>
    suspend fun getProviders(tvShowId: Int): Result<List<Provider>, Failure>
    suspend fun getTrailerUrl(tvShowId: Int): Result<String?, Failure>
    suspend fun getAccountStates(tvShowId: Int): Result<AccountStates, Failure>
    suspend fun rateTvShow(tvShowId: Int, rating: Int): Result<Unit, Failure>
    suspend fun removeRating(tvShowId: Int): Result<Unit, Failure>
    fun updateLanguageRelatedData()
    suspend fun getImagePaths(tvShowId: Int): Result<List<String>, Failure>
}