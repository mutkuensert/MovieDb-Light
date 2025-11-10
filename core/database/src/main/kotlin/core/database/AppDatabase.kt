package core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import core.database.account.FavoriteMovieDao
import core.database.account.FavoriteTvShowDao
import core.database.account.RatedMovieDao
import core.database.account.WatchlistMovieDao
import core.database.account.model.FavoriteMovieEntity
import core.database.account.model.FavoriteMovieIdEntity
import core.database.account.model.FavoriteTvShowEntity
import core.database.account.model.RatedMovieEntity
import core.database.account.model.WatchlistMovieEntity
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
        WatchlistMovieEntity::class,
        WatchlistMovieIdEntity::class,
        FavoriteMovieEntity::class,
        FavoriteMovieIdEntity::class,
        RatedMovieEntity::class,
        FavoriteTvShowEntity::class],
    version = 4,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = DatabaseMigrations.Schema1To2::class
        ),
        AutoMigration(
            from = 3,
            to = 4,
            spec = DatabaseMigrations.Schema3To4::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPopularMovieDao(): PopularMovieDao
    abstract fun getNowPlayingMovieDao(): NowPlayingMovieDao
    abstract fun getTopRateMovieDao(): TopRatedMovieDao
    abstract fun getUpcomingMovieDao(): UpcomingMovieDao
    abstract fun getSimilarMovieDao(): SimilarMovieDao
    abstract fun getFavoriteMovieDao(): FavoriteMovieDao
    abstract fun getWatchlistMovieDao(): WatchlistMovieDao
    abstract fun getRatedMovieDao(): RatedMovieDao
    abstract fun getFavoriteTvShowDao(): FavoriteTvShowDao
}


internal object DatabaseMigrations {
    @RenameTable(fromTableName = "FavoriteMovieEntity", toTableName = "FavoriteMovieIdEntity")
    @RenameTable(fromTableName = "WatchlistMovieEntity", toTableName = "WatchlistMovieIdEntity")
    class Schema1To2 : AutoMigrationSpec

    @DeleteColumn(tableName = "RatedMovieEntity", columnName = "rate")
    class Schema3To4 : AutoMigrationSpec

}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS `WatchlistMovieEntity`")
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `WatchlistMovieEntity` (
                `primaryKey` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `id` INTEGER NOT NULL,
                `page` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `posterPath` TEXT,
                `voteAverage` REAL
            )
        """
        )

        db.execSQL("DROP TABLE IF EXISTS `FavoriteMovieEntity`")
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `FavoriteMovieEntity` (
                `primaryKey` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `id` INTEGER NOT NULL,
                `page` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `posterPath` TEXT,
                `voteAverage` REAL
            )
        """
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `RatedMovieEntity` (
                `primaryKey` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `id` INTEGER NOT NULL,
                `page` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `posterPath` TEXT,
                `voteAverage` REAL,
                `rate` REAL
            )
        """
        )
    }
}

