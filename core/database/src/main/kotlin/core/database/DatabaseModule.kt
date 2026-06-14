package core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-database"
        ).addMigrations(MIGRATION_2_3, MIGRATION_4_5).build()
    }

    @Provides
    fun providePopularMovieDao(database: AppDatabase) = database.getPopularMovieDao()

    @Provides
    fun provideNowPlayingMovieDao(database: AppDatabase) = database.getNowPlayingMovieDao()

    @Provides
    fun provideUpcomingMovieDao(database: AppDatabase) = database.getUpcomingMovieDao()

    @Provides
    fun provideTopRatedMovieDao(database: AppDatabase) = database.getTopRateMovieDao()

    @Provides
    fun provideFavoriteMovieDao(database: AppDatabase) = database.getFavoriteMovieDao()

    @Provides
    fun provideWatchlistMovieDao(database: AppDatabase) = database.getWatchlistMovieDao()

    @Provides
    fun provideRatedMovieDao(database: AppDatabase) = database.getRatedMovieDao()

    @Provides
    fun provideFavoriteTvShowDao(database: AppDatabase) = database.getFavoriteTvShowDao()

    @Provides
    fun provideWatchlistTvShowDao(database: AppDatabase) = database.getWatchlistTvShowDao()

    @Provides
    fun provideRatedTvShowDao(database: AppDatabase) = database.getRatedTvShowDao()

    @Provides
    fun providePopularTvShowDao(database: AppDatabase) = database.getPopularTvShowDao()

    @Provides
    fun provideTvShowAiringTodayDao(database: AppDatabase) = database.getTvShowAiringTodayDao()

    @Provides
    fun provideUpcomingTvShowDao(database: AppDatabase) = database.getUpcomingTvShowDao()

    @Provides
    fun provideTopRatedTvShowDao(database: AppDatabase) = database.getTopRatedTvShowDao()

    @Provides
    @Singleton
    fun provideLanguagePreference(@ApplicationContext context: Context): LanguagePreference {
        return LanguagePreference(context)
    }
}
