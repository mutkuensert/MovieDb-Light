package core.ui.navigation

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class Navigator {
    lateinit var controller: NavHostController
        private set

    fun configure(controller: NavHostController) {
        this.controller = controller
    }

    inline fun <reified T> navigateToTab(tab: T) {
        val isAlreadySelected = controller.currentDestination?.hierarchy
            ?.any { it.hasRoute(tab::class) } == true
        controller.navigate(route = tab as Any) {
            val startDestination = controller.graph.findStartDestination()
            popUpTo(startDestination.id) {
                saveState = !isAlreadySelected
            }
            restoreState = !isAlreadySelected
            launchSingleTop = true
        }
    }

    fun navigateToRoute(route: Any) {
        controller.navigate(route)
    }

    fun navigateBack() {
        controller.popBackStack()
    }
}