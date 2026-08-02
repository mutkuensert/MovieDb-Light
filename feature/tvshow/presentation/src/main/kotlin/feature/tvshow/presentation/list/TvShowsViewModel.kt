package feature.tvshow.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import core.ui.navigation.Navigator
import core.ui.route.TvShowDetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import feature.tvshow.domain.TvShowRepository
import feature.tvshow.domain.model.TvShow
import feature.tvshow.presentation.list.model.TvShowUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(
    repository: TvShowRepository,
    private val navigator: Navigator,
) : ViewModel() {
    val popularTvShows = repository.getPopularTvShows().cachedIn(viewModelScope).asUiModelFlow()
    val tvShowsAiringToday = repository.getTvShowsAiringToday()
        .cachedIn(viewModelScope).asUiModelFlow()
    val topRatedTvShows = repository.getTopRatedTvShows().cachedIn(viewModelScope).asUiModelFlow()
    val upcomingTvShows = repository.getUpcomingTvShows().cachedIn(viewModelScope).asUiModelFlow()

    private fun Flow<PagingData<TvShow>>.asUiModelFlow(): Flow<PagingData<TvShowUiModel>> {
        return map { pagingData ->
            pagingData.map {
                TvShowUiModel(
                    it.id,
                    it.title,
                    it.imagePath,
                    it.voteAverage?.toString(),
                )
            }
        }
    }

    fun handleTvShowClick(tvShowId: Int) {
        navigator.navigateToRoute(TvShowDetailRoute(tvShowId))
    }
}
