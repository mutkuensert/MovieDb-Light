package filmcan

import filmcan.ui.MainViewModel
import filmcan.ui.home.HomeViewModel
import filmcan.ui.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SplashViewModel)
}
