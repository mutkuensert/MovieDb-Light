package injection

import feature.settings.data.SettingsRepositoryImpl
import feature.settings.domain.SettingsRepository
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}