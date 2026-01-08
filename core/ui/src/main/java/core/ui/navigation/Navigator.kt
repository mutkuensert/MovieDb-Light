package core.ui.navigation

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Navigator {
    lateinit var controller: NavHostController
        private set
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    fun configure(controller: NavHostController) {
        this.controller = controller
    }

    inline fun <reified T> navigateToTab(tab: T) {
        callAfterControllerInit {
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
    }

    fun navigateToRoute(route: Any) {
        callAfterControllerInit { controller.navigate(route) }
    }

    fun navigateBack() {
        callAfterControllerInit { controller.popBackStack() }
    }

    fun callAfterControllerInit(block: () -> Unit) {
        coroutineScope.launch {
            while (!::controller.isInitialized) {
                delay(300)
            }
            block.invoke()
        }
    }
}