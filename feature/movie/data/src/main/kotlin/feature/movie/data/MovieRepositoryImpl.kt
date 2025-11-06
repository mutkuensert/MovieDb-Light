package feature.movie.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.michaelbull.result.Result
import core.data.SessionManager
import core.data.network.mapToDomain
import core.data.util.withDecimals
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.similar.SimilarMovieDao
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieDao
import core.domain.AuthStateListener
import core.domain.ErrorMessage
import core.domain.common.Provider
import feature.movie.data.remote.MovieService
import feature.movie.domain.Movie
import feature.movie.domain.MovieDetails
import feature.movie.domain.MovieRepository
import feature.movie.domain.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import libraries.CountryManager
import libraries.image.TmdbImage

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl(
    private val movieService: MovieService,
    private val popularMovieDao: PopularMovieDao,
    private val nowPlayingMovieDao: NowPlayingMovieDao,
    private val upcomingMovieDao: UpcomingMovieDao,
    private val topRatedMovieDao: TopRatedMovieDao,
    private val similarMovieDao: SimilarMovieDao,
    private val sessionManager: SessionManager,
) : MovieRepository, AuthStateListener {
    private val popularMoviesPagingSourceFactory = InvalidatingPagingSourceFactory {
        popularMovieDao.getPagingSource()
    }
    private val nowPlayingMoviesPagingSourceFactory = InvalidatingPagingSourceFactory {
        nowPlayingMovieDao.getPagingSource()
    }
    private val upcomingMoviesPagingSourceFactory = InvalidatingPagingSourceFactory {
        upcomingMovieDao.getPagingSource()
    }
    private val topRatedMoviesPagingSourceFactory = InvalidatingPagingSourceFactory {
        topRatedMovieDao.getPagingSource()
    }
    private val similarMoviesPagingSourceFactory = InvalidatingPagingSourceFactory {
        similarMovieDao.getPagingSource()
    }

    override fun getPopularMovies(countryCode: String?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PopularMoviesRemoteMediator(
                { page -> movieService.getPopularMovies(page, countryCode) },
                popularMovieDao,
            ),
            pagingSourceFactory = { popularMoviesPagingSourceFactory() }
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


    override fun getMoviesNowPlaying(countryCode: String?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = NowPlayingMoviesRemoteMediator(
                { page -> movieService.getMoviesNowPlaying(page, countryCode) },
                nowPlayingMovieDao,
            ),
            pagingSourceFactory = { nowPlayingMoviesPagingSourceFactory() }
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

    override fun getUpcomingMovies(countryCode: String?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = UpcomingMoviesRemoteMediator(
                { page -> movieService.getUpcomingMovies(page, countryCode) },
                upcomingMovieDao,
            ),
            pagingSourceFactory = { upcomingMoviesPagingSourceFactory() }
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

    override fun getTopRatedMovies(countryCode: String?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = TopRatedMoviesRemoteMediator(
                { page -> movieService.getTopRatedMovies(page, countryCode) },
                topRatedMovieDao,
            ),
            pagingSourceFactory = { topRatedMoviesPagingSourceFactory() }
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

    override fun getSimilarMovies(movieId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = SimilarMoviesRemoteMediator(
                { page -> movieService.getSimilarMovies(movieId, page) },
                similarMovieDao,
            ),
            pagingSourceFactory = { similarMoviesPagingSourceFactory() }
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

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails, ErrorMessage> {
        return movieService.getMovieDetails(movieId).mapToDomain {
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

    override suspend fun getMovieCast(movieId: Int): Result<List<Person>, ErrorMessage> {
        return movieService.getMovieCredits(movieId).mapToDomain { response ->
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

    override suspend fun getProviders(movieId: Int): Result<List<Provider>, ErrorMessage> {
        return movieService.getProviders(movieId).mapToDomain { response ->
            val flatrate = response.results[CountryManager.current]?.flatrate
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

    override suspend fun getTrailerUrl(movieId: Int): Result<String?, ErrorMessage> {
        return movieService.getVideos(movieId).mapToDomain {
            val key = it.results.find { video ->
                (video.official && video.type == "Trailer" || video.type == "Trailer") && video.site.lowercase() == "youtube"
            }?.key
            key?.let { "https://www.youtube.com/watch?v=$key" }
        }
    }

    override suspend fun onUnauthorized() {
        popularMoviesPagingSourceFactory.invalidate()
        nowPlayingMoviesPagingSourceFactory.invalidate()
        topRatedMoviesPagingSourceFactory.invalidate()
        upcomingMoviesPagingSourceFactory.invalidate()
        similarMoviesPagingSourceFactory.invalidate()
    }
}