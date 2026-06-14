package feature.profile.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature.profile.domain.ProfileRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface InjectionModule {
    @Binds
    @Singleton
    fun bindProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository
}
