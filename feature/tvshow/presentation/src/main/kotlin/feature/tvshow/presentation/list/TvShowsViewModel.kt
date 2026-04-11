package feature.tvshow.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import core.ui.navigation.Navigator
import core.ui.route.TvShowDetailRoute
import feature.tvshow.domain.TvShowRepository
import feature.tvshow.domain.model.TvShow
import feature.tvshow.domain.usecase.SyncTvShowWatchlistStatusUseCase
import feature.tvshow.presentation.list.model.TvShowUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TvShowsViewModel(
    repository: TvShowRepository,
    private val syncTvShowWatchlistStatusUseCase: SyncTvShowWatchlistStatusUseCase,
    private val navigator: Navigator,
) : ViewModel() {
    val popularTvShows = repository.getPopularTvShows(null).asUiModelFlow()
    val tvShowsAiringToday = repository.getTvShowsAiringToday(null).asUiModelFlow()
    val topRatedTvShows = repository.getTopRatedTvShows(null).asUiModelFlow()
    val upcomingTvShows = repository.getUpcomingTvShows(null).asUiModelFlow()

    private fun Flow<PagingData<TvShow>>.asUiModelFlow(): Flow<PagingData<TvShowUiModel>> {
        return map { pagingData ->
            pagingData.map {
                TvShowUiModel(
                    it.id,
                    it.title,
                    it.imagePath,
                    it.voteAverage?.toString(),
                    it.inWatchlist
                )
            }
        }
    }

    fun handleWatchlistClick(tvShow: TvShowUiModel) {
        val inWatchlist = requireNotNull(tvShow.inWatchlist) {
            "Can't be null if button is visible"
        }
        viewModelScope.launch {
            syncTvShowWatchlistStatusUseCase(tvShow.id, !inWatchlist)
        }
    }

    fun handleTvShowClick(tvShowId: Int) {
        navigator.navigateToRoute(TvShowDetailRoute(tvShowId))
    }
}

private fun emptyPagingDataFlow(): Flow<PagingData<TvShowUiModel>> = flowOf(PagingData.empty())