package core.domain

import org.koin.dsl.module

val domainModule = module {
    factory { SyncMovieFavoriteStatusUseCase(get(), get()) }
    factory { SyncMovieWatchlistStatusUseCase(get(), get()) }
}
