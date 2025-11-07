package core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import core.database.account.AccountDao
import core.database.account.model.FavoriteMovieIdEntity
import core.database.account.model.FavoriteTvShowEntity
import core.database.account.model.WatchlistMovieIdEntity
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
        WatchlistMovieIdEntity::class,
        FavoriteMovieIdEntity::class,
        FavoriteTvShowEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = FirstAutoMigration::class
        ),
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun popularMovieDao(): PopularMovieDao
    abstract fun nowPlayingMovieDao(): NowPlayingMovieDao
    abstract fun topRateMovieDao(): TopRatedMovieDao
    abstract fun upcomingMovieDao(): UpcomingMovieDao
    abstract fun similarMovieDao(): SimilarMovieDao
    abstract fun accountDao(): AccountDao
}

@RenameTable(fromTableName = "FavoriteMovieEntity", toTableName = "FavoriteMovieIdEntity")
@RenameTable(fromTableName = "WatchlistMovieEntity", toTableName = "WatchlistMovieIdEntity")
class FirstAutoMigration : AutoMigrationSpec
