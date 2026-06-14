package feature.search.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import core.ui.navigation.Navigator
import core.ui.route.MovieDetailRoute
import core.ui.route.PersonDetailRoute
import core.ui.route.TvShowDetailRoute
import feature.search.domain.MultiResult
import feature.search.domain.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

@OptIn(FlowPreview::class)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val navigator: Navigator,
) : ViewModel() {
    private val _uiModel = MutableStateFlow(SearchUiModel.initial())
    val uiModel = _uiModel.asStateFlow()
    val trendingThisWeek = searchRepository.getTrendingThisWeek().map {
        it.map { multiResult -> multiResult.toUiModel() }
    }.cachedIn(viewModelScope)
    val searchResult: Flow<PagingData<ResultUiModel>> = getSearchResultFlow().map { pagingData ->
        pagingData.filter { it.imagePath != null }.map { multiResult -> multiResult.toUiModel() }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getSearchResultFlow(): Flow<PagingData<MultiResult>> {
        return uiModel.map { it.query }.debounce(750).flatMapLatest { query ->
            if (query.length > 1) {
                _uiModel.update { it.copy(showSearchResult = true) }
                searchRepository.search(query)
            } else {
                _uiModel.update { it.copy(showSearchResult = false) }
                flowOf(PagingData.empty())
            }
        }
    }

    fun handleQueryChange(query: String) {
        _uiModel.update { it.copy(query = query) }
    }

    fun handleDeleteQueryClick() {
        _uiModel.update { it.copy(query = "") }
    }

    fun handleMovieClick(id: Int) {
        navigator.navigateToRoute(MovieDetailRoute(id))
    }

    fun handleTvShowClick(id: Int) {
        navigator.navigateToRoute(TvShowDetailRoute(id))
    }

    fun handlePersonClick(id: Int) {
        navigator.navigateToRoute(PersonDetailRoute(id))
    }

    private fun MultiResult.toUiModel(): ResultUiModel {
        return when (this) {
            is MultiResult.Movie -> ResultUiModel.Movie(
                this.id,
                this.imagePath
            )

            is MultiResult.Person -> ResultUiModel.Person(
                this.id,
                this.imagePath
            )

            is MultiResult.TvShow -> ResultUiModel.TvShow(
                this.id,
                this.imagePath
            )
        }
    }
}
