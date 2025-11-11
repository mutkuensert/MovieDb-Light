package core.database

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "app-database"
        ).addMigrations(MIGRATION_2_3).build()
    }
    single { get<AppDatabase>().getPopularMovieDao() }
    single { get<AppDatabase>().getNowPlayingMovieDao() }
    single { get<AppDatabase>().getUpcomingMovieDao() }
    single { get<AppDatabase>().getTopRateMovieDao() }
    single { get<AppDatabase>().getSimilarMovieDao() }
    single { get<AppDatabase>().getFavoriteMovieDao() }
    single { get<AppDatabase>().getWatchlistMovieDao() }
    single { get<AppDatabase>().getRatedMovieDao() }
    single { get<AppDatabase>().getFavoriteTvShowDao() }
    single { LanguagePreference(androidContext()) }
}