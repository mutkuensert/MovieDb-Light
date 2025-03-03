package core.injection.common.core

import core.data.DefaultMyModelRepository
import core.data.MyModelRepository
import feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single<MyModelRepository> { DefaultMyModelRepository() }
    viewModelOf(::HomeViewModel)
}