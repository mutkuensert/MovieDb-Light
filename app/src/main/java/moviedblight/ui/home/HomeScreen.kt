package moviedblight.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import core.libraries.navigation.isRoute
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator
import feature.movies.presentation.MoviesScreen
import feature.profile.presentation.ProfileDeeplink
import feature.profile.presentation.ProfileScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navigator: Navigator) {
    val viewModel: HomeViewModel = koinViewModel()

    MainNavigation(
        navigator,
        viewModel::navigateToMovies,
        viewModel::navigateToProfile
    )
}

@Composable
fun MainNavigation(
    navigator: Navigator,
    onNavigateToMovies: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        navigator.configure(navController)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController,
                onNavigateToMovies,
                onNavigateToProfile
            )
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = NavTab.startDestination
        ) {
            composable<NavTab.MoviesRoute> {
                MoviesScreen()
            }

            composable<NavTab.ProfileRoute>(
                deepLinks = listOf(
                    navDeepLink<NavTab.ProfileRoute>(basePath = ProfileDeeplink)
                )
            ) {
                ProfileScreen()
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    navController: NavController,
    onNavigateToMovies: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navDestination by navController.currentTabDestinationAsState()

    NavigationBar(
        modifier = modifier,
        contentColor = Color.DarkGray
    ) {
        NavigationBarItem(
            selected = navDestination?.isRoute(NavTab.MoviesRoute::class) ?: false,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavTab.MoviesRoute.tabConfig.selectedColor,
                unselectedIconColor = NavTab.MoviesRoute.tabConfig.unselectedColor
            ),
            onClick = onNavigateToMovies,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Movie,
                    contentDescription = null
                )
            })

        NavigationBarItem(
            selected = navDestination?.isRoute(NavTab.ProfileRoute::class) ?: false,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavTab.MoviesRoute.tabConfig.selectedColor,
                unselectedIconColor = NavTab.MoviesRoute.tabConfig.unselectedColor
            ),
            onClick = onNavigateToProfile,
            icon = {
                Icon(
                    imageVector = Icons.Filled.PersonPin,
                    contentDescription = null
                )
            })
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun NavController.currentTabDestinationAsState(): State<NavDestination?> {
    val destination = remember { mutableStateOf<NavDestination?>(null) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, navDestination, _ ->
            if (NavTab.allRoutes.any { navDestination.route?.contains(it) == true })
                destination.value = navDestination
        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return destination
}
