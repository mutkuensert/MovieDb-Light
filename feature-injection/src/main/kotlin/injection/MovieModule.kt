package injection

import feature.movie.data.MovieRepositoryImpl
import feature.movie.data.remote.MovieService
import feature.movie.domain.MovieRepository
import feature.movie.presentation.RateMovieUseCase
import feature.movie.presentation.RemoveRatingUseCase
import feature.movie.presentation.detail.MovieDetailViewModel
import feature.movie.presentation.list.MoviesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val movieModule = module {
    single { get<Retrofit>().create(MovieService::class.java) }
    single<MovieRepository> {
        MovieRepositoryImpl(
            get(),
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
    factory { RateMovieUseCase(get(), get()) }
    factory { RemoveRatingUseCase(get(), get()) }
}