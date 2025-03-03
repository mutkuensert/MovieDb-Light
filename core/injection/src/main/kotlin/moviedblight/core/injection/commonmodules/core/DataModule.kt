package moviedblight.core.injection.commonmodules.core

import moviedblight.core.data.DefaultMyModelRepository
import moviedblight.core.data.MyModelRepository
import moviedblight.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single<MyModelRepository> { DefaultMyModelRepository() }
    viewModelOf(::HomeViewModel)
}