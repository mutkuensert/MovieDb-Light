package feature.tvshow.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import core.data.SessionManager
import core.data.auth.LogoutTrigger
import core.data.common.model.ReviewDto
import core.data.network.mapToDomain
import core.data.paging.TvShowsPagingSource
import core.data.util.withDecimals
import core.domain.AuthFailure
import core.domain.Failure
import core.domain.common.model.Provider
import core.domain.common.model.Review
import feature.tvshow.data.remote.TvShowService
import feature.tvshow.data.remote.response.PostTvShowRatingRequest
import feature.tvshow.domain.TvShowRepository
import feature.tvshow.domain.model.AccountStates
import feature.tvshow.domain.model.Person
import feature.tvshow.domain.model.TvShow
import feature.tvshow.domain.model.TvShowDetails
import filmcan.core.data.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import utils.LocalizationHelper
import utils.stringresource.StringResource
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class TvShowRepositoryImpl @Inject constructor(
    private val tvShowService: TvShowService,
    private val sessionManager: SessionManager,
    private val logoutTrigger: LogoutTrigger,
    private val stringResource: StringResource,
) : TvShowRepository {
    private val refreshTrigger = MutableStateFlow(0)

    override fun getPopularTvShows(countryCode: String?): Flow<PagingData<TvShow>> {
        return refreshTrigger.flatMapLatest {
            Pager(PagingConfig(pageSize = 20)) {
                TvShowsPagingSource { page ->
                    tvShowService.getPopularTvShows(page, countryCode)
                }
            }.flow.map { pagingData ->
                pagingData.map { tvShow ->
                    TvShow(
                        id = tvShow.id,
                        title = tvShow.name,
                        imagePath = tvShow.posterPath,
                        voteAverage = tvShow.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }


    override fun getTvShowsAiringToday(countryCode: String?): Flow<PagingData<TvShow>> {
        return refreshTrigger.flatMapLatest {
            Pager(PagingConfig(pageSize = 20)) {
                TvShowsPagingSource { page ->
                    tvShowService.getTvShowsAiringToday(page, countryCode)
                }
            }.flow.map { pagingData ->
                pagingData.map { tvShow ->
                    TvShow(
                        id = tvShow.id,
                        title = tvShow.name,
                        imagePath = tvShow.posterPath,
                        voteAverage = tvShow.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    override fun getUpcomingTvShows(countryCode: String?): Flow<PagingData<TvShow>> {
        return refreshTrigger.flatMapLatest {
            Pager(PagingConfig(pageSize = 20)) {
                TvShowsPagingSource { page ->
                    tvShowService.getUpcomingTvShows(page, countryCode)
                }
            }.flow.map { pagingData ->
                pagingData.map { tvShow ->
                    TvShow(
                        id = tvShow.id,
                        title = tvShow.name,
                        imagePath = tvShow.posterPath,
                        voteAverage = tvShow.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    override fun getTopRatedTvShows(countryCode: String?): Flow<PagingData<TvShow>> {
        return refreshTrigger.flatMapLatest {
            Pager(PagingConfig(pageSize = 20)) {
                TvShowsPagingSource { page ->
                    tvShowService.getTopRatedTvShows(page, countryCode)
                }
            }.flow.map { pagingData ->
                pagingData.map { tvShow ->
                    TvShow(
                        id = tvShow.id,
                        title = tvShow.name,
                        imagePath = tvShow.posterPath,
                        voteAverage = tvShow.voteAverage?.withDecimals(1),
                    )
                }
            }
        }
    }

    override suspend fun getTvShowDetails(tvShowId: Int): Result<TvShowDetails, Failure> {
        return tvShowService.getTvShowDetails(tvShowId).mapToDomain { response ->
            TvShowDetails(
                imagePath = response.posterPath,
                title = response.name ?: response.originalName,
                originalTitle = response.originalName,
                voteAverage = response.voteAverage?.withDecimals(1),
                runtime = response.episodeRunTime?.firstOrNull(),
                releaseDate = response.firstAirDate,
                genres = response.genres?.mapNotNull { it.name } ?: emptyList(),
                overview = response.overview
            )
        }
    }

    override suspend fun getCast(tvShowId: Int): Result<List<Person>, Failure> {
        return tvShowService.getTvShowCredits(tvShowId).mapToDomain { response ->
            response.cast.map {
                Person(
                    id = it.id,
                    imagePath = it.profilePath,
                    name = it.name,
                    character = it.character
                )
            }
        }
    }

    override suspend fun getProviders(tvShowId: Int): Result<List<Provider>, Failure> {
        return tvShowService.getProviders(tvShowId).mapToDomain { response ->
            val flatrate = response.results[LocalizationHelper.systemCountry]?.flatrate
                ?: response.results["US"]?.flatrate

            flatrate?.map {
                Provider(
                    it.providerId,
                    it.providerName,
                    it.logoPath
                )
            }?.distinctBy { it.name }?.distinctBy { it.id } ?: listOf()

        }
    }

    override suspend fun getTrailerYoutubeVideoId(tvShowId: Int): Result<String?, Failure> {
        return tvShowService.getVideos(tvShowId).mapToDomain {
            it.results.find { video ->
                (video.official && video.type == "Trailer" || video.type == "Trailer") && video.site.lowercase() == "youtube"
            }?.key
        }
    }

    override fun getReviewsPagingFlow(tvShowId: Int): Flow<PagingData<Review>> {
        return Pager(PagingConfig(pageSize = 20)) {
            TvShowReviewsPagingSource(
                getReviews = { page ->
                    tvShowService.getReviews(tvShowId, page)
                },
            )
        }.flow.map { pagingData ->
            pagingData.filter { reviewDto ->
                !reviewDto.content.isNullOrBlank()
                        && (!reviewDto.author.isNullOrBlank()
                        || !reviewDto.authorDetails?.name.isNullOrBlank()
                        || !reviewDto.authorDetails?.username.isNullOrBlank())
                        && reviewDto.createdAt != null
            }.map { it.toReview() }
        }
    }

    override suspend fun getReviews(tvShowId: Int): Result<List<Review>, Failure> {
        return tvShowService.getReviews(tvShowId)
            .mapToDomain { it.results?.map { review -> review.toReview() } ?: emptyList() }
    }

    override suspend fun getAccountStates(tvShowId: Int): Result<AccountStates, Failure> {
        val sessionId = sessionManager.getSessionId()
        if (sessionId == null) {
            logoutTrigger.triggerLogout()
            return Err(AuthFailure(stringResource.get(R.string.logged_out_unknown_reason)))
        }

        return tvShowService.getAccountStates(tvShowId, sessionId)
            .mapToDomain {
                AccountStates(it.id, it.favorite, it.rated?.value?.withDecimals(1), it.watchlist)
            }
    }

    override suspend fun rateTvShow(tvShowId: Int, rating: Int): Result<Unit, Failure> {
        val sessionId = sessionManager.getSessionId()
        if (sessionId == null) {
            logoutTrigger.triggerLogout()
            return Err(AuthFailure(stringResource.get(R.string.logged_out_unknown_reason)))
        }

        return tvShowService.rateTvShow(
            tvShowId,
            PostTvShowRatingRequest(rating),
            sessionId
        ).mapToDomain {}
    }

    override suspend fun removeRating(tvShowId: Int): Result<Unit, Failure> {
        val sessionId = sessionManager.getSessionId()
        if (sessionId == null) {
            logoutTrigger.triggerLogout()
            return Err(AuthFailure(stringResource.get(R.string.logged_out_unknown_reason)))
        }

        return tvShowService.deleteRating(tvShowId, sessionId).mapToDomain { }
    }

    override fun updateLanguageRelatedData() {
        refreshTrigger.update { it + 1 }
    }

    override suspend fun getImagePaths(tvShowId: Int): Result<List<String>, Failure> {
        return tvShowService.getImages(tvShowId).mapToDomain { response ->
            response.posters?.map { it.filePath } ?: emptyList()
        }
    }

    private fun ReviewDto.toReview(): Review {
        return Review(
            id = id,
            author = author ?: authorDetails?.name ?: authorDetails?.username ?: "",
            content = content!!,
            createdAt = createdAt!!,
            editedAt = updatedAt,
            rating = authorDetails?.rating,
        )
    }
}
