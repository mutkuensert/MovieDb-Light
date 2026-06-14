package feature.profile.domain

import core.domain.common.LanguageRelatedDataRefresher
import core.domain.profile.FavoriteMoviesRefresher
import core.domain.profile.FavoriteTvShowsRefresher
import core.domain.profile.RatedMoviesRefresher
import core.domain.profile.RatedTvShowsRefresher
import core.domain.profile.WatchlistMoviesRefresher
import core.domain.profile.WatchlistTvShowsRefresher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import feature.profile.domain.datarefresher.FavoriteMoviesRefresherImpl
import feature.profile.domain.datarefresher.FavoriteTvShowsRefresherImpl
import feature.profile.domain.datarefresher.LanguageRelatedDataRefresherImpl
import feature.profile.domain.datarefresher.RatedMoviesRefresherImpl
import feature.profile.domain.datarefresher.RatedTvShowsRefresherImpl
import feature.profile.domain.datarefresher.WatchlistMoviesRefresherImpl
import feature.profile.domain.datarefresher.WatchlistTvShowsRefresherImpl

@Module
@InstallIn(SingletonComponent::class)
interface InjectionModule {
    @Binds
    fun bindRatedMoviesRefresher(refresher: RatedMoviesRefresherImpl): RatedMoviesRefresher

    @Binds
    fun bindFavoriteMoviesRefresher(refresher: FavoriteMoviesRefresherImpl): FavoriteMoviesRefresher

    @Binds
    fun bindWatchlistMoviesRefresher(refresher: WatchlistMoviesRefresherImpl): WatchlistMoviesRefresher

    @Binds
    fun bindRatedTvShowsRefresher(refresher: RatedTvShowsRefresherImpl): RatedTvShowsRefresher

    @Binds
    fun bindFavoriteTvShowsRefresher(refresher: FavoriteTvShowsRefresherImpl): FavoriteTvShowsRefresher

    @Binds
    fun bindWatchlistTvShowsRefresher(refresher: WatchlistTvShowsRefresherImpl): WatchlistTvShowsRefresher

    @Binds
    @IntoSet
    fun bindLanguageRelatedDataRefresher(
        languageRelatedDataRefresher: LanguageRelatedDataRefresherImpl
    ): LanguageRelatedDataRefresher
}
