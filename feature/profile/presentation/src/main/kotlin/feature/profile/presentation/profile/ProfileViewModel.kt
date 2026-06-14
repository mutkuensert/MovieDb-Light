package feature.profile.presentation.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.michaelbull.result.onErr
import com.github.michaelbull.result.onOk
import core.domain.account.AccountRepository
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.LoginRoute
import core.ui.route.MovieDetailRoute
import core.ui.route.SettingsRoute
import core.ui.route.TvShowDetailRoute
import core.ui.showFailurePopup
import feature.profile.domain.ProfileRepository
import feature.profile.domain.usecase.LogoutUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val popupHandler: PopupHandler,
    private val logoutUseCase: LogoutUseCase,
    private val accountRepository: AccountRepository,
    private val navigator: Navigator,
    profileRepository: ProfileRepository,
) : ViewModel() {
    private val _uiModel = MutableStateFlow(ProfileUiModel.empty())
    val uiModel = _uiModel.asStateFlow()

    val favoriteMovies = uiModel.distinctUntilChanged { old, new ->
        old.favoriteMoviesSortBy == new.favoriteMoviesSortBy
    }.flatMapLatest { uiModel ->
        profileRepository.getFavoriteMovies(uiModel.favoriteMoviesSortBy.toDomain())
            .map { pagingData ->
                pagingData.map {
                    MovieUiModel(it.id, it.title, it.imagePath, it.voteAverage?.toString())
                }
            }
    }.cachedIn(viewModelScope)

    val watchlistMovies = uiModel.distinctUntilChanged { old, new ->
        old.watchlistMoviesSortBy == new.watchlistMoviesSortBy
    }.flatMapLatest { uiModel ->
        profileRepository.getWatchlistMovies(uiModel.watchlistMoviesSortBy.toDomain())
            .map { pagingData ->
                pagingData.map {
                    MovieUiModel(it.id, it.title, it.imagePath, it.voteAverage?.toString())
                }
            }
    }.cachedIn(viewModelScope)

    val ratedMovies = uiModel.distinctUntilChanged { old, new ->
        old.ratedMoviesSortBy == new.ratedMoviesSortBy
    }.flatMapLatest { uiModel ->
        profileRepository.getRatedMovies(uiModel.ratedMoviesSortBy.toDomain())
            .map { pagingData ->
                pagingData.map {
                    MovieUiModel(it.id, it.title, it.imagePath, it.voteAverage?.toString())
                }
            }
    }.cachedIn(viewModelScope)

    val favoriteTvShows = uiModel.distinctUntilChanged { old, new ->
        old.favoriteTvShowsSortBy == new.favoriteTvShowsSortBy
    }.flatMapLatest { uiModel ->
        profileRepository.getFavoriteTvShows(uiModel.favoriteTvShowsSortBy.toDomain())
            .map { pagingData ->
                pagingData.map {
                    MovieUiModel(it.id, it.title, it.imagePath, it.voteAverage?.toString())
                }
            }
    }.cachedIn(viewModelScope)

    val watchlistTvShows = uiModel.distinctUntilChanged { old, new ->
        old.watchlistTvShowsSortBy == new.watchlistTvShowsSortBy
    }.flatMapLatest { uiModel ->
        profileRepository.getWatchlistTvShows(uiModel.watchlistTvShowsSortBy.toDomain())
            .map { pagingData ->
                pagingData.map {
                    MovieUiModel(it.id, it.title, it.imagePath, it.voteAverage?.toString())
                }
            }
    }.cachedIn(viewModelScope)

    val ratedTvShows = uiModel.distinctUntilChanged { old, new ->
        old.ratedTvShowsSortBy == new.ratedTvShowsSortBy
    }.flatMapLatest { uiModel ->
        profileRepository.getRatedTvShows(uiModel.ratedTvShowsSortBy.toDomain())
            .map { pagingData ->
                pagingData.map {
                    MovieUiModel(it.id, it.title, it.imagePath, it.voteAverage?.toString())
                }
            }
    }.cachedIn(viewModelScope)

    fun initScreen() {
        viewModelScope.launch {
            accountRepository.getAccountDetails().onOk { user ->
                _uiModel.update { model ->
                    model.copy(
                        profileImagePath = user.profilePicturePath,
                        name = user.userName
                    )
                }
            }.onErr(popupHandler::showFailurePopup)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().onOk {
                navigator.popUpToRoute(LoginRoute())
            }.onErr(popupHandler::showFailurePopup)
        }
    }

    fun handleMovieClick(movieId: Int) {
        navigator.navigateToRoute(MovieDetailRoute(movieId))
    }

    fun handleTvShowClick(tvShowId: Int) {
        navigator.navigateToRoute(TvShowDetailRoute(tvShowId))
    }

    fun handleSortFavoriteMoviesClick(by: SortByUiModel) {
        _uiModel.update {
            it.copy(favoriteMoviesSortBy = by)
        }
    }

    fun handleSortWatchlistMoviesClick(by: SortByUiModel) {
        _uiModel.update {
            it.copy(watchlistMoviesSortBy = by)
        }
    }

    fun handleSortRatedMoviesClick(by: SortByUiModel) {
        _uiModel.update {
            it.copy(ratedMoviesSortBy = by)
        }
    }

    fun handleSortFavoriteTvShowsClick(by: SortByUiModel) {
        _uiModel.update {
            it.copy(favoriteTvShowsSortBy = by)
        }
    }

    fun handleSortWatchlistTvShowsClick(by: SortByUiModel) {
        _uiModel.update {
            it.copy(watchlistTvShowsSortBy = by)
        }
    }

    fun handleSortRatedTvShowsClick(by: SortByUiModel) {
        _uiModel.update {
            it.copy(ratedTvShowsSortBy = by)
        }
    }

    fun handleProfileClick() {
        navigator.navigateToRoute(SettingsRoute)
    }
}
