package feature.person

import feature.person.data.PersonRepositoryImpl
import feature.person.data.remote.PersonService
import feature.person.domain.PersonRepository
import feature.person.presentation.detail.PersonDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val personModule = module {
    single { get<Retrofit>().create(PersonService::class.java) }
    single<PersonRepository> { PersonRepositoryImpl(get(), get()) }
    viewModelOf(::PersonDetailViewModel)
}
