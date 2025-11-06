package injection

import core.domain.AuthStateListener
import feature.movie.data.MovieRepositoryImpl
import feature.movie.data.remote.MovieService
import feature.movie.domain.MovieRepository
import feature.movie.presentation.detail.MovieDetailViewModel
import feature.movie.presentation.list.MoviesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit

val movieModule = module {
    single { get<Retrofit>().create(MovieService::class.java) }
    single {
        MovieRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }.binds(arrayOf(MovieRepository::class, AuthStateListener::class))
    viewModelOf(::MoviesViewModel)
    viewModelOf(::MovieDetailViewModel)
}