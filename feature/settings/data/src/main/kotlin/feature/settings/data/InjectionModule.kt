package feature.settings.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature.settings.domain.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface InjectionModule {
    @Binds
    @Singleton
    fun bindSettingsRepository(settingsRepository: SettingsRepositoryImpl): SettingsRepository
}
