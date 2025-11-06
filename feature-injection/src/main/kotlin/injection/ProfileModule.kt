package injection

import feature.profile.domain.LogoutUseCase
import feature.profile.domain.StartSessionUseCase
import feature.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    viewModelOf(::ProfileViewModel)
    factory { LogoutUseCase(get(), getAll()) }
    factory { StartSessionUseCase(get(), get()) }
}