package core.injection.feature.movies

import feature.movie.data.MoviesRepositoryImpl
import feature.movie.data.remote.MovieService
import feature.movie.domain.GetMovieDetailsAndCastUseCase
import feature.movie.domain.MoviesRepository
import feature.movie.presentation.detail.MovieDetailViewModel
import feature.movie.presentation.list.MoviesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val movieModule = module {
    single { get<Retrofit>().create(MovieService::class.java) }
    single<MoviesRepository> {
        MoviesRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    viewModelOf(::MoviesViewModel)
    viewModelOf(::MovieDetailViewModel)
    factory { GetMovieDetailsAndCastUseCase(get()) }
}