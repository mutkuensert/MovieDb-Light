package core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import core.database.account.AccountDao
import core.database.account.model.FavoriteMovieEntity
import core.database.account.model.FavoriteTvShowEntity
import core.database.account.model.WatchlistMovieEntity
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.nowplaying.NowPlayingMovieEntity
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.popular.PopularMovieEntity
import core.database.feature.movies.similar.SimilarMovieDao
import core.database.feature.movies.similar.SimilarMovieEntity
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.toprated.TopRatedMovieEntity
import core.database.feature.movies.upcoming.UpcomingMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieEntity

@Database(
    entities = [PopularMovieEntity::class,
        NowPlayingMovieEntity::class,
        TopRatedMovieEntity::class,
        UpcomingMovieEntity::class,
        SimilarMovieEntity::class,
        WatchlistMovieEntity::class,
        FavoriteMovieEntity::class,
        FavoriteTvShowEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun popularMovieDao(): PopularMovieDao
    abstract fun nowPlayingMovieDao(): NowPlayingMovieDao
    abstract fun topRateMovieDao(): TopRatedMovieDao
    abstract fun upcomingMovieDao(): UpcomingMovieDao
    abstract fun similarMovieDao(): SimilarMovieDao
    abstract fun accountDao(): AccountDao
}
