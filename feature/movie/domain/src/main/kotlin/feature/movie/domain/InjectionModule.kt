package feature.movie.domain

import core.domain.common.LanguageRelatedDataRefresher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import feature.movie.domain.datarefresher.LanguageRelatedDataRefresherImpl

@Module
@InstallIn(SingletonComponent::class)
interface InjectionModule {
    @Binds
    @IntoSet
    fun bindLanguageRelatedDataRefresher(
        languageRelatedDataRefresher: LanguageRelatedDataRefresherImpl
    ): LanguageRelatedDataRefresher
}
