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
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieDao
import core.domain.AuthStateListener
import core.domain.ErrorMessage
import core.domain.common.Provider
import core.libraries.CountryManager
import core.libraries.image.TmdbImage
import feature.movie.data.remote.MovieService
import feature.movie.domain.Movie
import feature.movie.domain.MovieDetails
import feature.movie.domain.MovieRepository
import feature.movie.domain.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl(
    private val movieService: MovieService,
    private val popularMovieDao: PopularMovieDao,
    private val nowPlayingMovieDao: NowPlayingMovieDao,
    private val upcomingMovieDao: UpcomingMovieDao,
    private val topRatedMovieDao: TopRatedMovieDao,
    private val sessionManager: SessionManager,
) : MovieRepository, AuthStateListener {
    private var popularMovies: Flow<PagingData<Movie>> = getPopularMovies()
    private var moviesNowPlaying: Flow<PagingData<Movie>> = getMoviesNowPlaying()
    private var topRatedMovies: Flow<PagingData<Movie>> = getTopRatedMovies()
    private var upcomingMovies: Flow<PagingData<Movie>> = getUpcomingMovies()

    override fun getPopularMovies(countryCode: String?): Flow<PagingData<Movie>> {
        popularMovies = Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PopularMoviesRemoteMediator(
                { page -> movieService.getPopularMovies(page, countryCode) },
                popularMovieDao,
            ),
            pagingSourceFactory = { popularMovieDao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Movie(
                    id = entity.movie.id,
                    title = entity.movie.title,
                    imageUrl = entity.movie.posterPath?.let { TmdbImage.Poster(it).w780Url },
                    voteAverage = entity.movie.voteAverage.withDecimals(1),
                    isFavorite = entity.isFavorite.takeIf { sessionManager.loggedIn.value }
                )
            }
        }
        return popularMovies
    }


    override fun getMoviesNowPlaying(countryCode: String?): Flow<PagingData<Movie>> {
        moviesNowPlaying = Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = NowPlayingMoviesRemoteMediator(
                { page -> movieService.getMoviesNowPlaying(page, countryCode) },
                nowPlayingMovieDao,
            ),
            pagingSourceFactory = { nowPlayingMovieDao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Movie(
                    id = entity.movie.id,
                    title = entity.movie.title,
                    imageUrl = entity.movie.posterPath?.let { TmdbImage.Poster(it).w780Url },
                    voteAverage = entity.movie.voteAverage.withDecimals(1),
                    isFavorite = entity.isFavorite.takeIf { sessionManager.loggedIn.value }
                )
            }
        }
        return moviesNowPlaying
    }

    override fun getUpcomingMovies(countryCode: String?): Flow<PagingData<Movie>> {
        upcomingMovies = Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = UpcomingMoviesRemoteMediator(
                { page -> movieService.getUpcomingMovies(page, countryCode) },
                upcomingMovieDao,
            ),
            pagingSourceFactory = { upcomingMovieDao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Movie(
                    id = entity.movie.id,
                    title = entity.movie.title,
                    imageUrl = entity.movie.posterPath?.let { TmdbImage.Poster(it).w780Url },
                    voteAverage = entity.movie.voteAverage.withDecimals(1),
                    isFavorite = entity.isFavorite.takeIf { sessionManager.loggedIn.value }
                )
            }
        }
        return upcomingMovies
    }

    override fun getTopRatedMovies(countryCode: String?): Flow<PagingData<Movie>> {
        topRatedMovies = Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = TopRatedMoviesRemoteMediator(
                { page -> movieService.getTopRatedMovies(page, countryCode) },
                topRatedMovieDao,
            ),
            pagingSourceFactory = { topRatedMovieDao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Movie(
                    id = entity.movie.id,
                    title = entity.movie.title,
                    imageUrl = entity.movie.posterPath?.let { TmdbImage.Poster(it).w780Url },
                    voteAverage = entity.movie.voteAverage.withDecimals(1),
                    isFavorite = entity.isFavorite.takeIf { sessionManager.loggedIn.value }
                )
            }
        }
        return topRatedMovies
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
                    it.providerName,
                    it.logoPath?.let { path -> TmdbImage.Logo(path) }?.w300Url
                )
            }?.distinctBy { it.name } ?: listOf()

        }
    }

    override suspend fun onUnauthorized() {
        popularMovies = popularMovies.map { pagingData ->
            pagingData.map { it.copy(isFavorite = null) }
        }
        moviesNowPlaying = moviesNowPlaying.map { pagingData ->
            pagingData.map { it.copy(isFavorite = null) }
        }
        topRatedMovies = topRatedMovies.map { pagingData ->
            pagingData.map { it.copy(isFavorite = null) }
        }
        upcomingMovies = upcomingMovies.map { pagingData ->
            pagingData.map { it.copy(isFavorite = null) }
        }
    }
}