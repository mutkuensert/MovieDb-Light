package feature.search.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature.search.domain.SearchRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InjectionModule {
    @Binds
    @Singleton
    abstract fun bindSearchRepository(searchRepository: SearchRepositoryImpl): SearchRepository

    companion object {
        @Provides
        fun provideSearchService(retrofit: Retrofit): SearchService {
            return retrofit.create(SearchService::class.java)
        }

        @Provides
        fun provideTrendingService(retrofit: Retrofit): TrendingService {
            return retrofit.create(TrendingService::class.java)
        }
    }
}
