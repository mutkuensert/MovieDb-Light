package feature.movie.domain

import org.koin.dsl.module

val domainModule = module {
    factory { GetMovieDetailsAndCastUseCase(get()) }
}