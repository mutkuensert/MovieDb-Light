package feature.movies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import core.data.image.Poster
import core.database.feature.movies.popular.PopularMovieDao
import feature.movies.data.remote.MovieService
import feature.movies.domain.Movie
import feature.movies.domain.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepositoryImpl(
    private val movieService: MovieService,
    private val popularMovieDao: PopularMovieDao,
) : MoviesRepository {
    @OptIn(ExperimentalPagingApi::class)
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
                    isFavorite = null, //TODO: get from database
                    voteAverage = entity.voteAverage
                )
            }
        }
    }

    override fun getMoviesNowPlaying(): Flow<PagingData<Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToFavorites(movieId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        TODO("Not yet implemented")
    }
}