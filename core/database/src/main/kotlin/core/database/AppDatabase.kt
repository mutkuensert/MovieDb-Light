package core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import core.database.account.FavoriteMovieDao
import core.database.account.FavoriteTvShowDao
import core.database.account.RatedMovieDao
import core.database.account.RatedTvShowDao
import core.database.account.WatchlistMovieDao
import core.database.account.WatchlistTvShowDao
import core.database.account.model.FavoriteMovieEntity
import core.database.account.model.FavoriteMovieIdEntity
import core.database.account.model.FavoriteTvShowEntity
import core.database.account.model.FavoriteTvShowIdEntity
import core.database.account.model.RatedMovieEntity
import core.database.account.model.RatedTvShowEntity
import core.database.account.model.WatchlistMovieEntity
import core.database.account.model.WatchlistMovieIdEntity
import core.database.account.model.WatchlistTvShowEntity
import core.database.account.model.WatchlistTvShowIdEntity
import core.database.feature.movies.nowplaying.NowPlayingMovieDao
import core.database.feature.movies.nowplaying.NowPlayingMovieEntity
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.popular.PopularMovieEntity
import core.database.feature.movies.toprated.TopRatedMovieDao
import core.database.feature.movies.toprated.TopRatedMovieEntity
import core.database.feature.movies.upcoming.UpcomingMovieDao
import core.database.feature.movies.upcoming.UpcomingMovieEntity
import core.database.feature.tvshows.airingtoday.TvShowAiringTodayEntity
import core.database.feature.tvshows.airingtoday.TvShowsAiringTodayDao
import core.database.feature.tvshows.popular.PopularTvShowDao
import core.database.feature.tvshows.popular.PopularTvShowEntity
import core.database.feature.tvshows.toprated.TopRatedTvShowDao
import core.database.feature.tvshows.toprated.TopRatedTvShowEntity
import core.database.feature.tvshows.upcoming.UpcomingTvShowDao
import core.database.feature.tvshows.upcoming.UpcomingTvShowEntity

@Database(
    entities = [PopularMovieEntity::class,
        NowPlayingMovieEntity::class,
        TopRatedMovieEntity::class,
        UpcomingMovieEntity::class,
        WatchlistMovieEntity::class,
        WatchlistMovieIdEntity::class,
        FavoriteMovieEntity::class,
        FavoriteMovieIdEntity::class,
        RatedMovieEntity::class,
        FavoriteTvShowEntity::class,
        FavoriteTvShowIdEntity::class,
        WatchlistTvShowEntity::class,
        WatchlistTvShowIdEntity::class,
        PopularTvShowEntity::class,
        TvShowAiringTodayEntity::class,
        TopRatedTvShowEntity::class,
        UpcomingTvShowEntity::class,
        RatedTvShowEntity::class],
    version = 6,
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
        ),
        AutoMigration(
            from = 5,
            to = 6,
            spec = DatabaseMigrations.Schema5To6::class
        ),
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPopularMovieDao(): PopularMovieDao
    abstract fun getNowPlayingMovieDao(): NowPlayingMovieDao
    abstract fun getTopRateMovieDao(): TopRatedMovieDao
    abstract fun getUpcomingMovieDao(): UpcomingMovieDao
    abstract fun getFavoriteMovieDao(): FavoriteMovieDao
    abstract fun getWatchlistMovieDao(): WatchlistMovieDao
    abstract fun getRatedMovieDao(): RatedMovieDao
    abstract fun getFavoriteTvShowDao(): FavoriteTvShowDao
    abstract fun getWatchlistTvShowDao(): WatchlistTvShowDao
    abstract fun getRatedTvShowDao(): RatedTvShowDao
    abstract fun getPopularTvShowDao(): PopularTvShowDao
    abstract fun getTvShowAiringTodayDao(): TvShowsAiringTodayDao
    abstract fun getTopRatedTvShowDao(): TopRatedTvShowDao
    abstract fun getUpcomingTvShowDao(): UpcomingTvShowDao
}


internal object DatabaseMigrations {
    @RenameTable(fromTableName = "FavoriteMovieEntity", toTableName = "FavoriteMovieIdEntity")
    @RenameTable(fromTableName = "WatchlistMovieEntity", toTableName = "WatchlistMovieIdEntity")
    class Schema1To2 : AutoMigrationSpec

    @DeleteColumn(tableName = "RatedMovieEntity", columnName = "rate")
    class Schema3To4 : AutoMigrationSpec

    @DeleteTable(tableName = "SimilarMovieEntity")
    @DeleteTable(tableName = "SimilarTvShowEntity")
    class Schema5To6 : AutoMigrationSpec
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

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `FavoriteTvShowEntity_new` (
                `primaryKey` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `id` INTEGER NOT NULL,
                `page` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `posterPath` TEXT,
                `voteAverage` REAL
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO `FavoriteTvShowEntity_new` (
                `id`,
                `page`,
                `title`,
                `posterPath`,
                `voteAverage`
            )
            SELECT
                `id`,
                0,
                '',
                NULL,
                NULL
            FROM `FavoriteTvShowEntity`
        """.trimIndent()
        )

        db.execSQL("DROP TABLE `FavoriteTvShowEntity`")

        db.execSQL(
            """
            ALTER TABLE `FavoriteTvShowEntity_new`
            RENAME TO `FavoriteTvShowEntity`
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `FavoriteTvShowIdEntity` (
                `id` INTEGER PRIMARY KEY NOT NULL
            )
        """
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `RatedTvShowEntity` (
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
            CREATE TABLE IF NOT EXISTS `WatchlistTvShowEntity` (
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
            CREATE TABLE IF NOT EXISTS `PopularTvShowEntity` (
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
            CREATE TABLE IF NOT EXISTS `TvShowAiringTodayEntity` (
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
            CREATE TABLE IF NOT EXISTS `UpcomingTvShowEntity` (
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
            CREATE TABLE IF NOT EXISTS `SimilarTvShowEntity` (
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
            CREATE TABLE IF NOT EXISTS `TopRatedTvShowEntity` (
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
            CREATE TABLE IF NOT EXISTS `WatchlistTvShowIdEntity` (
                `id` INTEGER PRIMARY KEY NOT NULL
            )
        """
        )
    }
}