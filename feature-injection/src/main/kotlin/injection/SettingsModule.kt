package injection

import feature.settings.data.SettingsRepositoryImpl
import feature.settings.domain.SetContentLanguageUseCase
import feature.settings.domain.SettingsRepository
import feature.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    viewModelOf(::SettingsViewModel)
    factory { SetContentLanguageUseCase(get(), getAll()) }
}