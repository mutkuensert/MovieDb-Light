package feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import core.domain.AccountRepository
import core.ui.PopupHandler
import core.ui.navigation.Navigator
import core.ui.route.MovieDetailRoute
import feature.profile.domain.LogoutUseCase
import feature.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import libraries.image.TmdbImage

class ProfileViewModel(
    private val popupHandler: PopupHandler,
    private val logoutUseCase: LogoutUseCase,
    private val accountRepository: AccountRepository,
    private val navigator: Navigator,
    profileRepository: ProfileRepository,
) : ViewModel() {
    private val _uiModel = MutableStateFlow(ProfileUiModel.empty())
    val uiModel = _uiModel.asStateFlow()

    val favoriteMovies = profileRepository.getFavoriteMovies().map { pagingData ->
        pagingData.map {
            MovieUiModel(it.id, it.title, it.imageUrl, it.voteAverage?.toString())
        }
    }.cachedIn(viewModelScope)

    val watchlistMovies = profileRepository.getWatchlistMovies().map { pagingData ->
        pagingData.map {
            MovieUiModel(it.id, it.title, it.imageUrl, it.voteAverage?.toString())
        }
    }.cachedIn(viewModelScope)

    val ratedMovies = profileRepository.getRatedMovies().map { pagingData ->
        pagingData.map {
            MovieUiModel(it.id, it.title, it.imageUrl, it.voteAverage?.toString())
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
            }.onFailure {
                popupHandler.showSimpleMessage(it)
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            logoutUseCase.execute().onSuccess {
                navigator.navigateBack()
                navigator.navigateToRoute(LoginRoute())
            }.onFailure {
                popupHandler.showSimpleMessage(it)
            }
        }
    }

    fun handleMovieClick(movieId: Int) {
        navigator.navigateToRoute(MovieDetailRoute(movieId))
    }
}