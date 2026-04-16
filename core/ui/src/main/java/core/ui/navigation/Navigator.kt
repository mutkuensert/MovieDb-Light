package core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class Navigator {
    private val commands = MutableSharedFlow<NavCommand>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )

    @Composable
    fun NavigationExecutor(navController: NavHostController) {
        LaunchedEffect(navController) {
            commands.collect {
                when (it) {
                    is NavCommand.ToTab -> navController.navigateToTab(it.tab, it.reselected)
                    is NavCommand.ToRoute -> navController.navigate(it.route)
                    is NavCommand.Back -> navController.popBackStack()
                }
            }
        }
    }

    fun navigateToTab(tab: Any, reselected: Boolean) {
        commands.tryEmit(NavCommand.ToTab(tab, reselected))
    }

    fun navigateToRoute(route: Any) {
        commands.tryEmit(NavCommand.ToRoute(route))
    }

    fun navigateBack() {
        commands.tryEmit(NavCommand.Back)
    }

    private fun NavHostController.navigateToTab(route: Any, reselected: Boolean) {
        navigate(route) {
            val startDestination = graph.findStartDestination()
            popUpTo(startDestination.id) {
                saveState = !reselected
            }
            restoreState = !reselected
            launchSingleTop = true
        }
    }
}

private sealed interface NavCommand {
    class ToRoute(val route: Any) : NavCommand
    class ToTab(val tab: Any, val reselected: Boolean) : NavCommand
    object Back : NavCommand
}