package moviedblight.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator
import feature.movies.presentation.MoviesRoute
import feature.movies.presentation.MoviesScreen
import feature.profile.presentation.ProfileDeeplink
import feature.profile.presentation.ProfileRoute
import feature.profile.presentation.ProfileScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navigator: Navigator) {
    val viewModel: HomeViewModel = koinViewModel()
    val statusBarContentColor by viewModel.statusBarContentColor.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        Spacer(
            Modifier
                .fillMaxWidth()
                .background(statusBarContentColor)
                .windowInsetsTopHeight(WindowInsets.statusBars)
        )

        MainNavigation(
            navigator,
            viewModel::navigateToMovies,
            viewModel::navigateToProfile
        )
    }
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
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
            navController = navController,
            startDestination = NavTab.MovieTab
        ) {
            navigation<NavTab.MovieTab>(MoviesRoute) {
                composable<MoviesRoute> {
                    MoviesScreen()
                }
            }

            navigation<NavTab.ProfileTab>(ProfileRoute()) {
                composable<ProfileRoute>(
                    deepLinks = listOf(
                        navDeepLink<ProfileRoute>(basePath = ProfileDeeplink)
                    )
                ) {
                    ProfileScreen()
                }
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = modifier.height(80.dp),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any {
                it.hasRoute(NavTab.MovieTab::class)
            } == true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onBackground,
                unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                indicatorColor = Color.Gray
            ),
            onClick = onNavigateToMovies,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Movie,
                    contentDescription = null
                )
            })

        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any {
                it.hasRoute(NavTab.ProfileTab::class)
            } == true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onBackground,
                unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                indicatorColor = Color.Gray
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
