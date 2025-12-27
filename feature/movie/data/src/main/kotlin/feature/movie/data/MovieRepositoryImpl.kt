package feature.movie.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.michaelbull.result.Result
import core.data.SessionManager
import core.data.network.mapToDomain
import core.data.util.withDecimals
import core.database.LanguagePreference
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.similar.SimilarMovieDao
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieDao
import core.domain.common.listener.RemoteContentLanguagePreferenceChangeListener
import core.domain.Failure
import core.domain.common.model.Provider
import core.domain.movie.AccountStates
import feature.movie.data.remote.MovieService
import feature.movie.data.remote.response.PostMovieRatingRequest
import feature.movie.domain.Movie
import feature.movie.domain.MovieDetails
import feature.movie.domain.MovieRepository
import feature.movie.domain.Person
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import libraries.LocalizationHelper
import libraries.getYoutubeUrlByKey
import libraries.image.TmdbImage

@OptIn(
    ExperimentalPagingApi::class,
    ExperimentalCoroutinesApi::class
)
class MovieRepositoryImpl(
    private val movieService: MovieService,
    private val popularMovieDao: PopularMovieDao,
    private val nowPlayingMovieDao: NowPlayingMovieDao,
    private val upcomingMovieDao: UpcomingMovieDao,
    private val topRatedMovieDao: TopRatedMovieDao,
    private val similarMovieDao: SimilarMovieDao,
    private val sessionManager: SessionManager,
    private val languagePreference: LanguagePreference,
) : MovieRepository, RemoteContentLanguagePreferenceChangeListener {

    private val refreshTrigger = MutableStateFlow(0)

    override fun getPopularMovies(countryCode: String?): Flow<PagingData<Movie>> {
        return refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = PopularMoviesRemoteMediator(
                    getMovies = { page ->
                        movieService.getPopularMovies(
                            page,
                            countryCode,
                            languagePreference.getLanguageTag()
                        )
                    },
                    popularMovieDao
                ),
                pagingSourceFactory = { popularMovieDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.movie.id,
                        title = entity.movie.title,
                        imageUrl = entity.movie.posterPath?.let { TmdbImage.Poster(it).w780Url },
                        voteAverage = entity.movie.voteAverage?.withDecimals(1),
                        inWatchlist = entity.inWatchlist.takeIf { sessionManager.loggedIn.value }
                    )
                }
            }
        }
    }


    override fun getMoviesNowPlaying(countryCode: String?): Flow<PagingData<Movie>> {
        return refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = NowPlayingMoviesRemoteMediator(
                    getMovies = { page ->
                        movieService.getMoviesNowPlaying(
                            page,
                            countryCode,
                            languagePreference.getLanguageTag()
                        )
                    },
                    nowPlayingMovieDao
                ),
                pagingSourceFactory = { nowPlayingMovieDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.movie.id,
                        title = entity.movie.title,
                        imageUrl = entity.movie.posterPath?.let { TmdbImage.Poster(it).w780Url },
                        voteAverage = entity.movie.voteAverage?.withDecimals(1),
                        inWatchlist = entity.inWatchlist.takeIf { sessionManager.loggedIn.value }
                    )
                }
            }
        }
    }

    override fun getUpcomingMovies(countryCode: String?): Flow<PagingData<Movie>> {
        return refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = UpcomingMoviesRemoteMediator(
                    getMovies = { page ->
                        movieService.getUpcomingMovies(
                            page,
                            countryCode,
                            languagePreference.getLanguageTag()
                        )
                    },
                    upcomingMovieDao
                ),
                pagingSourceFactory = { upcomingMovieDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.movie.id,
                        title = entity.movie.title,
                        imageUrl = entity.movie.posterPath?.let { TmdbImage.Poster(it).w780Url },
                        voteAverage = entity.movie.voteAverage?.withDecimals(1),
                        inWatchlist = entity.inWatchlist.takeIf { sessionManager.loggedIn.value }
                    )
                }
            }
        }
    }

    override fun getTopRatedMovies(countryCode: String?): Flow<PagingData<Movie>> {
        return refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = TopRatedMoviesRemoteMediator(
                    getMovies = { page ->
                        movieService.getTopRatedMovies(
                            page,
                            countryCode,
                            languagePreference.getLanguageTag()
                        )
                    },
                    topRatedMovieDao
                ),
                pagingSourceFactory = { topRatedMovieDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.movie.id,
                        title = entity.movie.title,
                        imageUrl = entity.movie.posterPath?.let { TmdbImage.Poster(it).w780Url },
                        voteAverage = entity.movie.voteAverage?.withDecimals(1),
                        inWatchlist = entity.inWatchlist.takeIf { sessionManager.loggedIn.value }
                    )
                }
            }
        }
    }

    override fun getSimilarMovies(movieId: Int): Flow<PagingData<Movie>> {
        return refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = SimilarMoviesRemoteMediator(
                    getMovies = { page ->
                        movieService.getSimilarMovies(
                            movieId, page,
                            languagePreference.getLanguageTag()
                        )
                    },
                    similarMovieDao
                ),
                pagingSourceFactory = { similarMovieDao.getPagingSource() }
            ).flow.map { pagingData ->
                pagingData.map { entity ->
                    Movie(
                        id = entity.movie.id,
                        title = entity.movie.title,
                        imageUrl = entity.movie.posterPath?.let { TmdbImage.Poster(it).w780Url },
                        voteAverage = entity.movie.voteAverage?.withDecimals(1),
                        inWatchlist = entity.inWatchlist.takeIf { sessionManager.loggedIn.value }
                    )
                }
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails, Failure> {
        return movieService.getMovieDetails(
            movieId,
            languagePreference.getLanguageTag()
        ).mapToDomain {
            MovieDetails(
                imageUrl = it.posterPath?.let { path -> TmdbImage.Poster(path) }?.originalSizedUrl,
                title = it.originalTitle,
                voteAverage = it.voteAverage?.withDecimals(1),
                runtime = it.runtime,
                releaseDate = it.releaseDate,
                overview = it.overview
            )
        }
    }

    override suspend fun getMovieCast(movieId: Int): Result<List<Person>, Failure> {
        return movieService.getMovieCredits(movieId, languagePreference.getLanguageTag())
            .mapToDomain { response ->
                response.cast.map {
                    Person(
                        id = it.id,
                        imageUrl = it.profilePath?.let { path -> TmdbImage.Profile(path) }?.h632Url,
                        name = it.name,
                        character = it.character
                    )
                }
            }
    }

    override suspend fun getProviders(movieId: Int): Result<List<Provider>, Failure> {
        return movieService.getProviders(movieId).mapToDomain { response ->
            val flatrate = response.results[LocalizationHelper.systemCountry]?.flatrate
                ?: response.results["US"]?.flatrate

            flatrate?.map {
                Provider(
                    it.providerId,
                    it.providerName,
                    it.logoPath?.let { path -> TmdbImage.Logo(path) }?.w300Url
                )
            }?.distinctBy { it.name }?.distinctBy { it.id } ?: listOf()

        }
    }

    override suspend fun getTrailerUrl(movieId: Int): Result<String?, Failure> {
        return movieService.getVideos(movieId, languagePreference.getLanguageTag()).mapToDomain {
            val key = it.results.find { video ->
                (video.official && video.type == "Trailer" || video.type == "Trailer") && video.site.lowercase() == "youtube"
            }?.key
            key?.let { getYoutubeUrlByKey(key) }
        }
    }

    override suspend fun getAccountStates(movieId: Int): Result<AccountStates, Failure> {
        return movieService.getAccountStates(movieId, sessionManager.requireSessionId())
            .mapToDomain {
                AccountStates(it.id, it.favorite, it.rated?.value?.withDecimals(1), it.watchlist)
            }
    }

    override suspend fun rateMovie(movieId: Int, rating: Int): Result<Unit, Failure> {
        return movieService.rateMovie(
            movieId,
            PostMovieRatingRequest(rating),
            sessionManager.requireSessionId()
        ).mapToDomain {}
    }

    override suspend fun removeRating(movieId: Int): Result<Unit, Failure> {
        return movieService.deleteRating(movieId, sessionManager.requireSessionId()).mapToDomain { }
    }

    override fun onRemoteContentLanguagePreferenceChanged() {
        refreshTrigger.update { it + 1 }
    }
}
