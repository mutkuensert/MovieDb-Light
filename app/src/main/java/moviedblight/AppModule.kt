package moviedblight

import core.libraries.AppScope
import moviedblight.ui.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single(createdAtStart = true) { AuthEventHandler(get<AppScope>(), get(), get()) }
    viewModelOf(::HomeViewModel)
}