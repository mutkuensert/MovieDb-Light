package feature.tvshow.presentation.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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


@HiltViewModel
class TvShowsViewModel @Inject constructor(
    repository: TvShowRepository,
    private val syncTvShowWatchlistStatusUseCase: SyncTvShowWatchlistStatusUseCase,
    private val navigator: Navigator,
) : ViewModel() {
    val popularTvShows = repository.getPopularTvShows(null)
        .cachedIn(viewModelScope).asUiModelFlow()
    val tvShowsAiringToday = repository.getTvShowsAiringToday(null)
        .cachedIn(viewModelScope).asUiModelFlow()
    val topRatedTvShows = repository.getTopRatedTvShows(null)
        .cachedIn(viewModelScope).asUiModelFlow()
    val upcomingTvShows = repository.getUpcomingTvShows(null)
        .cachedIn(viewModelScope).asUiModelFlow()

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