package feature.tvshow.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.github.michaelbull.result.Result
import core.data.SessionManager
import core.data.common.model.ReviewDto
import core.data.network.mapToDomain
import core.data.util.withDecimals
import core.database.LanguagePreference
import core.database.feature.tvshows.airingtoday.TvShowsAiringTodayDao
import core.database.feature.tvshows.popular.PopularTvShowDao
import core.database.feature.tvshows.toprated.TopRatedTvShowDao
import core.database.feature.tvshows.upcoming.UpcomingTvShowDao
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import utils.LocalizationHelper

@OptIn(
    ExperimentalPagingApi::class,
    ExperimentalCoroutinesApi::class
)
class TvShowRepositoryImpl(
    private val tvShowService: TvShowService,
    private val popularTvShowDao: PopularTvShowDao,
    private val tvShowAiringTodayDao: TvShowsAiringTodayDao,
    private val upcomingTvShowDao: UpcomingTvShowDao,
    private val topRatedTvShowDao: TopRatedTvShowDao,
    private val sessionManager: SessionManager,
    private val languagePreference: LanguagePreference,
) : TvShowRepository {
    private val refreshTrigger = MutableStateFlow(0)

    override fun getPopularTvShows(countryCode: String?): Flow<PagingData<TvShow>> {
        return refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = PopularTvShowsRemoteMediator(
                    getTvShows = { page ->
                        tvShowService.getPopularTvShows(
                            page,
                            countryCode,
                            languagePreference.getLanguageTag()
                        )
                    },
                    popularTvShowDao
                ),
                pagingSourceFactory = { popularTvShowDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    TvShow(
                        id = entity.tvShow.id,
                        title = entity.tvShow.title,
                        imagePath = entity.tvShow.posterPath,
                        voteAverage = entity.tvShow.voteAverage?.withDecimals(1),
                        inWatchlist = entity.inWatchlist.takeIf { sessionManager.loggedIn.value }
                    )
                }
            }
        }
    }


    override fun getTvShowsAiringToday(countryCode: String?): Flow<PagingData<TvShow>> {
        return refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = TvShowsAiringTodayRemoteMediator(
                    getTvShows = { page ->
                        tvShowService.getTvShowsAiringToday(
                            page,
                            countryCode,
                            languagePreference.getLanguageTag()
                        )
                    },
                    tvShowAiringTodayDao
                ),
                pagingSourceFactory = { tvShowAiringTodayDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    TvShow(
                        id = entity.tvShow.id,
                        title = entity.tvShow.title,
                        imagePath = entity.tvShow.posterPath,
                        voteAverage = entity.tvShow.voteAverage?.withDecimals(1),
                        inWatchlist = entity.inWatchlist.takeIf { sessionManager.loggedIn.value }
                    )
                }
            }
        }
    }

    override fun getUpcomingTvShows(countryCode: String?): Flow<PagingData<TvShow>> {
        return refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = UpcomingTvShowsRemoteMediator(
                    getUpcomingTvShows = { page ->
                        tvShowService.getUpcomingTvShows(
                            page,
                            countryCode,
                            languagePreference.getLanguageTag()
                        )
                    },
                    upcomingTvShowDao
                ),
                pagingSourceFactory = { upcomingTvShowDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    TvShow(
                        id = entity.tvShow.id,
                        title = entity.tvShow.title,
                        imagePath = entity.tvShow.posterPath,
                        voteAverage = entity.tvShow.voteAverage?.withDecimals(1),
                        inWatchlist = entity.inWatchlist.takeIf { sessionManager.loggedIn.value }
                    )
                }
            }
        }
    }

    override fun getTopRatedTvShows(countryCode: String?): Flow<PagingData<TvShow>> {
        return refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = TopRatedTvShowsRemoteMediator(
                    getTvShows = { page ->
                        tvShowService.getTopRatedTvShows(
                            page,
                            countryCode,
                            languagePreference.getLanguageTag()
                        )
                    },
                    topRatedTvShowDao
                ),
                pagingSourceFactory = { topRatedTvShowDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    TvShow(
                        id = entity.tvShow.id,
                        title = entity.tvShow.title,
                        imagePath = entity.tvShow.posterPath,
                        voteAverage = entity.tvShow.voteAverage?.withDecimals(1),
                        inWatchlist = entity.inWatchlist.takeIf { sessionManager.loggedIn.value }
                    )
                }
            }
        }
    }

    override suspend fun getTvShowDetails(tvShowId: Int): Result<TvShowDetails, Failure> {
        return tvShowService.getTvShowDetails(
            tvShowId,
            languagePreference.getLanguageTag()
        ).mapToDomain { response ->
            TvShowDetails(
                imagePath = response.posterPath,
                title = response.name ?: response.originalName,
                voteAverage = response.voteAverage?.withDecimals(1),
                runtime = response.episodeRunTime?.firstOrNull(),
                releaseDate = response.firstAirDate,
                genres = response.genres?.mapNotNull { it.name } ?: emptyList(),
                overview = response.overview
            )
        }
    }

    override suspend fun getCast(tvShowId: Int): Result<List<Person>, Failure> {
        return tvShowService.getTvShowCredits(tvShowId, languagePreference.getLanguageTag())
            .mapToDomain { response ->
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
        return tvShowService.getVideos(tvShowId, languagePreference.getLanguageTag()).mapToDomain {
            it.results.find { video ->
                (video.official && video.type == "Trailer" || video.type == "Trailer") && video.site.lowercase() == "youtube"
            }?.key
        }
    }

    override fun getReviews(tvShowId: Int): Flow<PagingData<Review>> {
        return Pager(PagingConfig(pageSize = 20)) {
            TvShowReviewsPagingSource(
                getReviews = { page ->
                    tvShowService.getReviews(tvShowId, languagePreference.getLanguageTag(), page)
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

    override suspend fun getFirstReview(tvShowId: Int): Result<Review?, Failure> {
        return tvShowService.getReviews(tvShowId, languagePreference.getLanguageTag())
            .mapToDomain { it.results?.firstOrNull()?.toReview() }
    }

    override suspend fun getAccountStates(tvShowId: Int): Result<AccountStates, Failure> {
        return tvShowService.getAccountStates(tvShowId, sessionManager.requireSessionId())
            .mapToDomain {
                AccountStates(it.id, it.favorite, it.rated?.value?.withDecimals(1), it.watchlist)
            }
    }

    override suspend fun rateTvShow(tvShowId: Int, rating: Int): Result<Unit, Failure> {
        return tvShowService.rateTvShow(
            tvShowId,
            PostTvShowRatingRequest(rating),
            sessionManager.requireSessionId()
        ).mapToDomain {}
    }

    override suspend fun removeRating(tvShowId: Int): Result<Unit, Failure> {
        return tvShowService.deleteRating(tvShowId, sessionManager.requireSessionId())
            .mapToDomain { }
    }

    override fun updateLanguageRelatedData() {
        refreshTrigger.update { it + 1 }
    }

    override suspend fun getImagePaths(tvShowId: Int): Result<List<String>, Failure> {
        return tvShowService.getImages(tvShowId, language = languagePreference.getLanguageTag())
            .mapToDomain { response ->
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
