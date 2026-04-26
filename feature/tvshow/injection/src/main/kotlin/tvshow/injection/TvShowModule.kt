package tvshow.injection

import core.domain.common.LanguageRelatedDataRefresher
import feature.tvshow.data.TvShowRepositoryImpl
import feature.tvshow.data.remote.TvShowService
import feature.tvshow.domain.TvShowRepository
import feature.tvshow.domain.datarefresher.LanguageRelatedDataRefresherImpl
import feature.tvshow.domain.usecase.RateTvShowUseCase
import feature.tvshow.domain.usecase.RemoveRatingUseCase
import feature.tvshow.domain.usecase.SyncTvShowFavoriteStatusUseCase
import feature.tvshow.domain.usecase.SyncTvShowWatchlistStatusUseCase
import feature.tvshow.presentation.detail.TvShowDetailViewModel
import feature.tvshow.presentation.list.TvShowsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val tvShowModule = module {
    single { get<Retrofit>().create(TvShowService::class.java) }
    single<TvShowRepository> {
        TvShowRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    viewModelOf(::TvShowsViewModel)
    viewModelOf(::TvShowDetailViewModel)
    factory { RateTvShowUseCase(get(), get()) }
    factory { RemoveRatingUseCase(get(), get()) }
    factory { SyncTvShowFavoriteStatusUseCase(get(), get()) }
    factory { SyncTvShowWatchlistStatusUseCase(get(), get()) }
    factory { LanguageRelatedDataRefresherImpl(get()) }.bind(LanguageRelatedDataRefresher::class)
}
