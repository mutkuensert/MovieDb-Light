package injection

import core.domain.AppContentLanguageChangeListener
import core.domain.profile.ProfileFavoriteMoviesPagingInvalidator
import core.domain.profile.ProfileRatedMoviesPagingInvalidator
import core.domain.profile.ProfileWatchlistMoviesPagingInvalidator
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
            ProfileFavoriteMoviesPagingInvalidator::class,
            ProfileWatchlistMoviesPagingInvalidator::class,
            ProfileRatedMoviesPagingInvalidator::class,
            AppContentLanguageChangeListener::class
        )
    )

    viewModelOf(::LoginViewModel)
}