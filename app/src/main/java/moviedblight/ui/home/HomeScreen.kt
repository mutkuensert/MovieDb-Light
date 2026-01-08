package moviedblight.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import core.ui.Popup
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator
import core.ui.route.LoginRoute
import core.ui.route.MovieDetailRoute
import core.ui.route.MoviesRoute
import core.ui.route.SettingsRoute
import feature.movie.presentation.R
import feature.movie.presentation.detail.MovieDetailScreen
import feature.movie.presentation.list.MoviesScreen
import feature.profile.presentation.login.LoginDeeplink
import feature.profile.presentation.login.LoginScreen
import feature.profile.presentation.profile.ProfileRoute
import feature.profile.presentation.profile.ProfileScreen
import feature.search.presentation.SearchRoute
import feature.search.presentation.SearchScreen
import feature.settings.presentation.SettingsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navigator: Navigator) {
    val viewModel: HomeViewModel = koinViewModel()
    val statusBarContentColor by viewModel.statusBarContentColor.collectAsStateWithLifecycle()
    val loading by viewModel.loadingAnimator.loading.collectAsStateWithLifecycle()
    val popup by viewModel.popupHandler.popup.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        Spacer(
            Modifier
                .fillMaxWidth()
                .background(statusBarContentColor)
                .windowInsetsTopHeight(WindowInsets.statusBars)
        )

        Box {
            val navController = rememberNavController()

            LaunchedEffect(navController) {
                navigator.configure(navController)
            }

            MainNavigation(
                navController,
                viewModel::navigateToMovies,
                viewModel::navigateToSearch,
                viewModel::navigateToProfile
            )

            if (loading) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = Color(0x75000000))
                ) {
                    CircularProgressIndicator(
                        Modifier
                            .size(40.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            if (popup != null) {
                Popup(popup!!, viewModel)
            }
        }
    }
}

@Composable
private fun Popup(popup: Popup, viewModel: HomeViewModel) {
    AlertDialog(
        title = {
            if (popup.title != null) {
                Text(popup.title!!)
            }
        },
        text = { Text(popup.message) },
        onDismissRequest = viewModel::closePopup,
        confirmButton = {
            if (popup.showConfirmButton) {
                TextButton({
                    viewModel.closePopup()
                    popup.onConfirm()
                }) {
                    Text(stringResource(R.string.ok))
                }
            }
        },
        dismissButton = if (popup.showDismissButton) {
            {
                TextButton({
                    viewModel.closePopup()
                    popup.onDismiss()
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        } else null,
        containerColor = MaterialTheme.colorScheme.background,
        properties = DialogProperties(
            dismissOnBackPress = popup.allowToDismiss,
            dismissOnClickOutside = popup.allowToDismiss
        )
    )
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    onNavigateToMovies: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController,
                onNavigateToMovies,
                onNavigateToSearch,
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

                composable<MovieDetailRoute> {
                    MovieDetailScreen()
                }
            }

            navigation<NavTab.SearchTab>(SearchRoute) {
                composable<SearchRoute> {
                    SearchScreen()
                }

                composable<MovieDetailRoute> {
                    MovieDetailScreen()
                }
            }

            navigation<NavTab.ProfileTab>(LoginRoute()) {
                composable<LoginRoute>(
                    deepLinks = listOf(
                        navDeepLink<LoginRoute>(basePath = LoginDeeplink)
                    )
                ) {
                    LoginScreen()
                }

                composable<ProfileRoute> {
                    ProfileScreen()
                }

                composable<SettingsRoute> {
                    SettingsScreen()
                }

                composable<MovieDetailRoute> {
                    MovieDetailScreen()
                }
            }
        }
    }
}


@Composable
private fun BottomNavBar(
    navController: NavController,
    onNavigateToMovies: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = modifier.height(100.dp),
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
                it.hasRoute(NavTab.SearchTab::class)
            } == true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onBackground,
                unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                indicatorColor = Color.Gray
            ),
            onClick = onNavigateToSearch,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Search,
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
