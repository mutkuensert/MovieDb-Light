package feature.profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.domain.account.AccountRepository
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.LoginRoute
import core.ui.route.MovieDetailRoute
import core.ui.route.SettingsRoute
import core.ui.showFailurePopup
import feature.profile.domain.usecase.LogoutUseCase
import feature.profile.domain.ProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import libraries.image.TmdbImage

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
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
                    MovieUiModel(it.id, it.title, it.imageUrl, it.voteAverage?.toString())
                }
            }
    }.cachedIn(viewModelScope)

    val watchlistMovies = uiModel.distinctUntilChanged { old, new ->
        old.watchlistMoviesSortBy == new.watchlistMoviesSortBy
    }.flatMapLatest { uiModel ->
        profileRepository.getWatchlistMovies(uiModel.watchlistMoviesSortBy.toDomain())
            .map { pagingData ->
                pagingData.map {
                    MovieUiModel(it.id, it.title, it.imageUrl, it.voteAverage?.toString())
                }
            }
    }.cachedIn(viewModelScope)

    val ratedMovies = uiModel.distinctUntilChanged { old, new ->
        old.ratedMoviesSortBy == new.ratedMoviesSortBy
    }.flatMapLatest { uiModel ->
        profileRepository.getRatedMovies(uiModel.ratedMoviesSortBy.toDomain())
            .map { pagingData ->
                pagingData.map {
                    MovieUiModel(it.id, it.title, it.imageUrl, it.voteAverage?.toString())
                }
            }
    }.cachedIn(viewModelScope)

    fun initScreen() {
        viewModelScope.launch {
            accountRepository.fetchAccountDetails().onSuccess { user ->
                _uiModel.update { model ->
                    model.copy(
                        profileImageUrl = user.profilePicturePath?.let { path ->
                            TmdbImage.Profile(path)
                        }?.w185Url,
                        name = user.userName
                    )
                }
            }.onFailure(popupHandler::showFailurePopup)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                navigator.navigateBack()
                navigator.navigateToRoute(LoginRoute())
            }.onFailure(popupHandler::showFailurePopup)
        }
    }

    fun handleMovieClick(movieId: Int) {
        navigator.navigateToRoute(MovieDetailRoute(movieId))
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

    fun handleProfilePictureClick() {
        navigator.navigateToRoute(SettingsRoute)
    }
}