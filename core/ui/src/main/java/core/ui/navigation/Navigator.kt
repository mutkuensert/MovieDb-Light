package core.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class Navigator {
    lateinit var controller: NavHostController
        private set

    fun configure(controller: NavHostController) {
        this.controller = controller
    }

    fun navigateToTab(tab: NavTab) {
        controller.navigate(tab) {
            val startDestination = controller.graph.findStartDestination()
            popUpTo(startDestination.id) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }

    fun navigateToRoute(route: Any) {
        controller.navigate(route)
    }
}