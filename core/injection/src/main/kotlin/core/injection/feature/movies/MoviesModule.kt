package core.injection.feature.movies

import feature.movies.data.MoviesRepositoryImpl
import feature.movies.data.remote.MovieService
import feature.movies.domain.MoviesRepository
import feature.movies.presentation.MoviesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val moviesModule = module {
    single { get<Retrofit>().create(MovieService::class.java) }
    single<MoviesRepository> { MoviesRepositoryImpl(get(), get()) }
    viewModelOf(::MoviesViewModel)
}