package injection

import core.domain.common.listener.RemoteContentLanguagePreferenceChangeListener
import core.domain.profile.FavoriteMovieChangeListener
import core.domain.profile.MovieRateChangeListener
import core.domain.profile.MovieWatchlistChangeListener
import feature.profile.data.ProfileRepositoryImpl
import feature.profile.domain.LogoutUseCase
import feature.profile.domain.ProfileRepository
import feature.profile.domain.StartSessionUseCase
import feature.profile.presentation.login.LoginViewModel
import feature.profile.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

val profileModule = module {
    viewModelOf(::ProfileViewModel)
    factory { LogoutUseCase(get(), getAll()) }
    factory { StartSessionUseCase(get(), get()) }
    single {
        ProfileRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }.binds(
        arrayOf(
            ProfileRepository::class,
            FavoriteMovieChangeListener::class,
            MovieWatchlistChangeListener::class,
            MovieRateChangeListener::class,
            RemoteContentLanguagePreferenceChangeListener::class
        )
    )

    viewModelOf(::LoginViewModel)
}