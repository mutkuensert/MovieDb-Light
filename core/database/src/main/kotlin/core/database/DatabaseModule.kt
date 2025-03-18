package core.database

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java, "app-database"
        ).build()
    }
    single { get<AppDatabase>().popularMovieDao() }
    single { get<AppDatabase>().nowPlayingMovieDao() }
    single { get<AppDatabase>().upcomingMovieDao() }
    single { get<AppDatabase>().topRateMovieDao() }
}