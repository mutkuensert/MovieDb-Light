package feature.movie.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature.movie.data.remote.MovieService
import feature.movie.domain.MovieRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InjectionModule {
    @Binds
    @Singleton
    abstract fun bindMovieRepository(movieRepository: MovieRepositoryImpl): MovieRepository

    companion object {
        @Provides
        fun provideMovieService(retrofit: Retrofit): MovieService {
            return retrofit.create(MovieService::class.java)
        }
    }
}
