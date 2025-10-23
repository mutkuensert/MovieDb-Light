package core.ui.navigation

import android.annotation.SuppressLint
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class Navigator {
    lateinit var controller: NavHostController
        private set

    fun configure(controller: NavHostController) {
        this.controller = controller
    }

    @SuppressLint("RestrictedApi")
    fun navigateToTab(tab: NavTab) {
        controller.navigate(tab) {
            popUpTo(controller.graph.findStartDestination().id) {
                inclusive = true
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}