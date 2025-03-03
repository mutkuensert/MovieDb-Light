package feature.home

import androidx.lifecycle.ViewModel
import core.ui.navigation.routes.MoviesRoute
import core.ui.navigation.Navigator

class HomeViewModel(
    private val navigator: Navigator,
) : ViewModel() {

    fun navigateToMovies()  {
        navigator.controller.navigate(MoviesRoute)
    }
}
