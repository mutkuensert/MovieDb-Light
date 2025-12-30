package feature.search

import feature.search.data.SearchRepositoryImpl
import feature.search.data.SearchService
import feature.search.domain.SearchRepository
import feature.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val searchModule = module {
    single { get<Retrofit>().create(SearchService::class.java) }
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    viewModelOf(::SearchViewModel)
}