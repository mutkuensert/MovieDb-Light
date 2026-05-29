package filmcan

import filmcan.ui.MainViewModel
import filmcan.ui.home.HomeViewModel
import feature.splash.presentation.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SplashViewModel)
}
