package core.injection.feature.movies

import feature.movie.data.MoviesRepositoryImpl
import feature.movie.data.remote.MovieService
import feature.movie.domain.MoviesRepository
import feature.movie.presentation.MoviesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val moviesModule = module {
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
}