package core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import core.database.feature.movies.popular.PopularMovieDao
import core.database.feature.movies.popular.PopularMovieEntity

@Database(entities = [PopularMovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun popularMovieDao(): PopularMovieDao
}