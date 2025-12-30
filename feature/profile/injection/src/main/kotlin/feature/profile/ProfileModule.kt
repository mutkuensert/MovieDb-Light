package feature.profile

import core.domain.common.LanguageRelatedDataRefresher
import core.domain.profile.FavoriteMoviesRefresher
import core.domain.profile.RatedMoviesRefresher
import core.domain.profile.WatchlistMoviesRefresher
import feature.profile.data.ProfileRepositoryImpl
import feature.profile.domain.ProfileRepository
import feature.profile.domain.datarefresher.FavoriteMoviesRefresherImpl
import feature.profile.domain.datarefresher.LanguageRelatedDataRefresherImpl
import feature.profile.domain.datarefresher.RatedMoviesRefresherImpl
import feature.profile.domain.datarefresher.WatchlistMoviesRefresherImpl
import feature.profile.domain.usecase.LogoutUseCase
import feature.profile.domain.usecase.StartSessionUseCase
import feature.profile.presentation.login.LoginViewModel
import feature.profile.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileModule = module {
    viewModelOf(::ProfileViewModel)
    factory { LogoutUseCase(get(), get()) }
    factory { StartSessionUseCase(get(), get()) }
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModelOf(::LoginViewModel)
    single<RatedMoviesRefresher> { RatedMoviesRefresherImpl(get()) }
    single<FavoriteMoviesRefresher> { FavoriteMoviesRefresherImpl(get()) }
    single<WatchlistMoviesRefresher> { WatchlistMoviesRefresherImpl(get()) }
    single { LanguageRelatedDataRefresherImpl(get()) }.bind(LanguageRelatedDataRefresher::class)
}