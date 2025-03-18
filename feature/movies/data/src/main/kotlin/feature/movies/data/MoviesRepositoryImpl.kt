package feature.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import core.data.image.Poster
import core.data.util.withDecimals
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieDao
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
) : MoviesRepository {

    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PopularMoviesRemoteMediator(
                movieService::getPopularMovies,
                popularMovieDao
            ),
            pagingSourceFactory = { popularMovieDao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    imageUrl = entity.posterPath?.let { Poster(it).w780Url },
                    voteAverage = entity.voteAverage.withDecimals(1)
                )
            }
        }
    }


    override fun getMoviesNowPlaying(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = NowPlayingMoviesRemoteMediator(
                movieService::getMoviesNowPlaying,
                nowPlayingMovieDao
            ),
            pagingSourceFactory = { nowPlayingMovieDao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    imageUrl = entity.posterPath?.let { Poster(it).w780Url },
                    voteAverage = entity.voteAverage.withDecimals(1)
                )
            }
        }
    }

    override fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = UpcomingMoviesRemoteMediator(
                movieService::getUpcomingMovies,
                upcomingMovieDao
            ),
            pagingSourceFactory = { upcomingMovieDao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    imageUrl = entity.posterPath?.let { Poster(it).w780Url },
                    voteAverage = entity.voteAverage.withDecimals(1)
                )
            }
        }
    }

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = TopRatedMoviesRemoteMediator(
                movieService::getTopRatedMovies,
                topRatedMovieDao
            ),
            pagingSourceFactory = { topRatedMovieDao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    imageUrl = entity.posterPath?.let { Poster(it).w780Url },
                    voteAverage = entity.voteAverage.withDecimals(1)
                )
            }
        }
    }
}