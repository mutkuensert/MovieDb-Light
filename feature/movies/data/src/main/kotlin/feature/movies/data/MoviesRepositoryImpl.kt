package feature.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import core.data.SessionManager
import core.data.util.withDecimals
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieDao
import core.libraries.image.TmdbImage
import feature.movies.data.remote.MovieService
import feature.movies.domain.Movie
import feature.movies.domain.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class MoviesRepositoryImpl(
    private val movieService: MovieService,
    private val popularMovieDao: PopularMovieDao,
    private val nowPlayingMovieDao: NowPlayingMovieDao,
    private val upcomingMovieDao: UpcomingMovieDao,
    private val topRatedMovieDao: TopRatedMovieDao,
    private val sessionManager: SessionManager,
) : MoviesRepository {

    override fun getPopularMovies(countryCode: String?): Flow<PagingData<Movie>> {
        return Pager(
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
    }


    override fun getMoviesNowPlaying(countryCode: String?): Flow<PagingData<Movie>> {
        return Pager(
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
    }

    override fun getUpcomingMovies(countryCode: String?): Flow<PagingData<Movie>> {
        return Pager(
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
    }

    override fun getTopRatedMovies(countryCode: String?): Flow<PagingData<Movie>> {
        return Pager(
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
    }
}