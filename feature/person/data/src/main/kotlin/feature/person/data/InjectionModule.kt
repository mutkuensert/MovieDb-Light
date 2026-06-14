package feature.person.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature.person.data.remote.PersonService
import feature.person.domain.PersonRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InjectionModule {
    @Binds
    @Singleton
    abstract fun bindPersonRepository(personRepository: PersonRepositoryImpl): PersonRepository

    companion object {
        @Provides
        fun providePersonService(retrofit: Retrofit): PersonService {
            return retrofit.create(PersonService::class.java)
        }
    }
}
