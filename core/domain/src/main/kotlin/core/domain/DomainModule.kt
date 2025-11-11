package core.domain

import core.domain.movie.SyncMovieFavoriteStatusUseCase
import core.domain.movie.SyncMovieWatchlistStatusUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { SyncMovieFavoriteStatusUseCase(get(), get()) }
    factory { SyncMovieWatchlistStatusUseCase(get(), get()) }
}
