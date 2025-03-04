package moviedblight.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import core.ui.navigation.Navigator
import core.ui.navigation.routes.MoviesRoute
import feature.movies.presentation.MoviesScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navigator: Navigator) {
    val viewModel: HomeViewModel = koinViewModel()

    MainNavigation(
        navigator,
        viewModel::navigateToMovies
    )
}

@Composable
fun MainNavigation(navigator: Navigator, onNavigateToMovies: () -> Unit) {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        navigator.setNavController(navController)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController, onNavigateToMovies)
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = MoviesRoute
        ) {
            composable<MoviesRoute> {
                MoviesScreen()
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    navController: NavController,
    onNavigateToMovies: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navDestination by navController.currentTabDestinationAsState()
    NavigationBar(
        modifier = modifier,
        contentColor = Color.DarkGray
    ) {
        NavigationBarItem(
            selected = MoviesRoute.navDestinationId == navDestination?.id,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MoviesRoute.tabConfig.selectedColor,
                unselectedIconColor = MoviesRoute.tabConfig.unselectedColor
            ),
            onClick = onNavigateToMovies,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null
                )
            })

        /*NavigationBarItem(
            selected = lastTabItemRoute == TabItem.TvShow.route,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToTvShow,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tvshow),
                    contentDescription = null
                )
            })

        NavigationBarItem(
            selected = lastTabItemRoute == TabItem.MultiSearch.route,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToMultiSearch,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            })

        NavigationBarItem(
            selected = lastTabItemRoute == TabItem.Login.route,
            unselectedContentColor = Color.Gray,
            selectedContentColor = AppColors.DarkCyan,
            onClick = viewModel::navigateToLogin,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_account),
                    contentDescription = null
                )
            })*/
    }
}

val navTabs = listOf(MoviesRoute)

@SuppressLint("RestrictedApi")
@Composable
private fun NavController.currentTabDestinationAsState(): State<NavDestination?> {
    val destination = remember { mutableStateOf<NavDestination?>(null) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, navDestination, _ ->
            if (navTabs.any { navDestination.id == it.navDestinationId })
                destination.value = navDestination
        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return destination
}
