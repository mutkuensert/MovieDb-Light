package moviedblight.home

import androidx.lifecycle.ViewModel
import moviedblight.core.ui.navigation.routes.MoviesRoute
import moviedblight.core.ui.navigation.Navigator

class HomeViewModel(
    private val navigator: Navigator,
) : ViewModel() {

    fun navigateToMovies()  {
        navigator.controller.navigate(MoviesRoute)
    }
}
