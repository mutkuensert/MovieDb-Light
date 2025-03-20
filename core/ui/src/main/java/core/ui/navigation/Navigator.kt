package core.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Navigator {
    lateinit var controller: NavHostController
        private set

    private val _currentTab = MutableStateFlow<NavDestination?>(null)
    val currentTab: StateFlow<NavDestination?> = _currentTab.asStateFlow()

    fun configure(controller: NavHostController) {
        this.controller = controller
        keepTrackOfCurrentTab()
    }

    private fun keepTrackOfCurrentTab() {
        val listener = NavController.OnDestinationChangedListener { _, navDestination, _ ->
            if (NavTab.all.any { navDestination.id == it.navDestinationId })
                _currentTab.value = navDestination
        }
        controller.addOnDestinationChangedListener(listener)
    }

    fun <T : Any> navigateToTab(route: T) {
        controller.navigate(route) {
            if (currentTab.value?.route != null) {
                popUpTo(currentTab.value!!.route!!) {
                    saveState = true
                    inclusive = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}