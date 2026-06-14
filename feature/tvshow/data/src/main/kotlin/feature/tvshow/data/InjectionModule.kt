package feature.tvshow.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature.tvshow.data.remote.TvShowService
import feature.tvshow.domain.TvShowRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InjectionModule {
    @Binds
    @Singleton
    abstract fun bindTvShowRepository(tvShowRepository: TvShowRepositoryImpl): TvShowRepository

    companion object {
        @Provides
        fun provideTvShowService(retrofit: Retrofit): TvShowService {
            return retrofit.create(TvShowService::class.java)
        }
    }
}
