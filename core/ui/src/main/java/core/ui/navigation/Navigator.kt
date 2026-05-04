package core.ui.navigation

import androidx.activity.compose.LocalActivity
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
        val activity = LocalActivity.current
        LaunchedEffect(navController) {
            commands.collect {
                when (it) {
                    is NavCommand.ToTab -> navController.navigateToTab(it.tab, it.reselected)
                    is NavCommand.ToRoute -> navController.navigate(it.route)
                    is NavCommand.PopUpToRoute -> navController.popUpToRoute(it.route, it.inclusive)
                    is NavCommand.Back -> navController.popBackStack()
                    NavCommand.CloseApp -> activity?.finish()
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

    fun popUpToRoute(route: Any, inclusive: Boolean = false) {
        commands.tryEmit(NavCommand.PopUpToRoute(route, inclusive))
    }

    fun navigateBack() {
        commands.tryEmit(NavCommand.Back)
    }

    fun closeApp() {
        commands.tryEmit(NavCommand.CloseApp)
    }

    private fun NavHostController.navigateToTab(route: Any, reselected: Boolean) {
        val currentTabGraphRoute = currentBackStackEntry?.destination?.parent?.route

        navigate(route) {
            if (currentTabGraphRoute != null) {
                popUpTo(currentTabGraphRoute) {
                    inclusive = true
                    saveState = !reselected
                }
            }
            restoreState = !reselected
            launchSingleTop = true
        }
    }

    private fun NavHostController.popUpToRoute(route: Any, inclusive: Boolean) {
        navigate(route) {
            if (isInBackStack(route)) {
                popUpTo(route) {
                    this.inclusive = inclusive
                }
            } else {
                val startDestination = graph.findStartDestination()
                popUpTo(
                    currentBackStackEntry
                        ?.destination
                        ?.parent //Current tab
                        ?.id ?: startDestination.id
                ) {
                    this.inclusive = true
                }
            }
        }
    }
}

private sealed interface NavCommand {
    class ToRoute(val route: Any) : NavCommand
    class PopUpToRoute(val route: Any, val inclusive: Boolean = false) : NavCommand
    class ToTab(val tab: Any, val reselected: Boolean) : NavCommand
    object Back : NavCommand

    object CloseApp : NavCommand
}
