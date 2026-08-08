package filmcan.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PersonPin
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import androidx.navigation.toRoute
import core.ui.Popup
import core.ui.navigation.NavTab
import core.ui.navigation.Navigator
import core.ui.route.AboutRoute
import core.ui.route.LoginRoute
import core.ui.route.MovieDetailRoute
import core.ui.route.MovieReviewsRoute
import core.ui.route.MoviesRoute
import core.ui.route.PersonDetailRoute
import core.ui.route.SettingsRoute
import core.ui.route.SplashRoute
import core.ui.route.TvShowDetailRoute
import core.ui.route.TvShowReviewsRoute
import core.ui.route.TvShowsRoute
import feature.movie.presentation.R
import feature.movie.presentation.detail.MovieDetailScreen
import feature.movie.presentation.list.MoviesScreen
import feature.movie.presentation.reviews.MovieReviewsScreen
import feature.person.presentation.detail.PersonDetailScreen
import feature.profile.presentation.about.AboutScreen
import feature.profile.presentation.login.LoginDeeplink
import feature.profile.presentation.login.LoginScreen
import feature.profile.presentation.profile.ProfileRoute
import feature.profile.presentation.profile.ProfileScreen
import feature.search.presentation.SearchRoute
import feature.search.presentation.SearchScreen
import feature.settings.presentation.SettingsScreen
import feature.splash.presentation.SplashScreen
import feature.tvshow.presentation.detail.TvShowDetailScreen
import feature.tvshow.presentation.list.TvShowsScreen
import feature.tvshow.presentation.reviews.TvShowReviewsScreen
import kotlin.reflect.KClass

@Composable
fun HomeScreen(navigator: Navigator) {
    val viewModel: HomeViewModel = hiltViewModel()
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
            navigator.NavigationExecutor(navController)

            MainNavigation(
                navController,
                viewModel.isProfileFeatureEnabled,
                viewModel::navigateToMovies,
                viewModel::navigateToTvShows,
                viewModel::navigateToSearch,
                viewModel::navigateToProfile,
                viewModel::navigateToAbout
            )

            if (loading) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) {}
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
    isProfileTabEnabled: Boolean,
    onNavigateToMovies: (reselected: Boolean) -> Unit,
    onNavigateToTvShows: (reselected: Boolean) -> Unit,
    onNavigateToSearch: (reselected: Boolean) -> Unit,
    onNavigateToProfile: (reselected: Boolean) -> Unit,
    onNavigateToAbout: (reselected: Boolean) -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val shouldShowBottomBar = navBackStackEntry?.destination?.hasRoute(SplashRoute::class) == false

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavBar(
                    navController,
                    isProfileTabEnabled,
                    onNavigateToMovies,
                    onNavigateToTvShows,
                    onNavigateToSearch,
                    onNavigateToProfile,
                    onNavigateToAbout
                )
            }
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
            navController = navController,
            startDestination = SplashRoute
        ) {
            composable<SplashRoute> {
                SplashScreen()
            }

            navigation<NavTab.MovieTab>(MoviesRoute) {
                composable<MoviesRoute> {
                    MoviesScreen()
                }

                composable<MovieDetailRoute> {
                    MovieDetailScreen()
                }

                composable<MovieReviewsRoute> {
                    MovieReviewsScreen()
                }

                composable<PersonDetailRoute> {
                    PersonDetailScreen()
                }
            }

            navigation<NavTab.TvShowTab>(TvShowsRoute) {
                composable<TvShowsRoute> {
                    TvShowsScreen()
                }

                composable<TvShowDetailRoute> {
                    TvShowDetailScreen()
                }

                composable<TvShowReviewsRoute> {
                    TvShowReviewsScreen()
                }

                composable<PersonDetailRoute> {
                    PersonDetailScreen()
                }
            }

            navigation<NavTab.SearchTab>(SearchRoute) {
                composable<SearchRoute> {
                    SearchScreen()
                }

                composable<MovieDetailRoute> {
                    MovieDetailScreen()
                }

                composable<MovieReviewsRoute> {
                    MovieReviewsScreen()
                }

                composable<PersonDetailRoute> {
                    PersonDetailScreen()
                }

                composable<TvShowDetailRoute> {
                    TvShowDetailScreen()
                }

                composable<TvShowReviewsRoute> {
                    TvShowReviewsScreen()
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

                composable<AboutRoute> { backStackEntry ->
                    AboutScreen(backStackEntry.toRoute<AboutRoute>())
                }

                composable<MovieDetailRoute> {
                    MovieDetailScreen()
                }

                composable<MovieReviewsRoute> {
                    MovieReviewsScreen()
                }

                composable<PersonDetailRoute> {
                    PersonDetailScreen()
                }

                composable<TvShowDetailRoute> {
                    TvShowDetailScreen()
                }

                composable<TvShowReviewsRoute> {
                    TvShowReviewsScreen()
                }
            }

            navigation<NavTab.AboutTab>(AboutRoute()) {
                composable<AboutRoute> { backStackEntry ->
                    AboutScreen(backStackEntry.toRoute<AboutRoute>())
                }
            }
        }
    }
}


@Composable
private fun BottomNavBar(
    navController: NavController,
    isProfileTabEnabled: Boolean,
    onNavigateToMovies: (reselected: Boolean) -> Unit,
    onNavigateToTvShows: (reselected: Boolean) -> Unit,
    onNavigateToSearch: (reselected: Boolean) -> Unit,
    onNavigateToProfile: (reselected: Boolean) -> Unit,
    onNavigateToAbout: (reselected: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.height(100.dp),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        BarItem(
            navController,
            NavTab.MovieTab::class,
            Icons.Outlined.Movie,
            onNavigateToMovies::invoke
        )

        BarItem(
            navController,
            NavTab.TvShowTab::class,
            Icons.Outlined.LiveTv,
            onNavigateToTvShows::invoke
        )

        BarItem(
            navController,
            NavTab.SearchTab::class,
            Icons.Outlined.Search,
            onNavigateToSearch::invoke
        )

        if (isProfileTabEnabled) {
            BarItem(
                navController,
                NavTab.ProfileTab::class,
                Icons.Outlined.PersonPin,
                onNavigateToProfile::invoke
            )
        } else {
            BarItem(
                navController,
                NavTab.AboutTab::class,
                Icons.Outlined.Info,
                onNavigateToAbout::invoke
            )
        }
    }
}

@Composable
private inline fun RowScope.BarItem(
    navController: NavController,
    tab: KClass<*>,
    icon: ImageVector,
    crossinline onClick: (selected: Boolean) -> Unit,
) {
    val selected = navController.isTabSelected(tab)
    NavigationBarItem(
        selected = selected,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onBackground,
            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
            indicatorColor = Color.Gray
        ),
        onClick = { onClick(selected) },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        })
}

@Composable
private fun NavController.isTabSelected(tab: KClass<*>): Boolean {
    return this.currentBackStackEntryAsState().value
        ?.destination
        ?.hierarchy
        ?.any { it.hasRoute(tab) } == true
}
