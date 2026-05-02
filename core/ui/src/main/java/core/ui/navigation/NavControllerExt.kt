package core.ui.navigation

import androidx.navigation.NavController

fun <T : Any> NavController.isInBackStack(route: T): Boolean {
    return try {
        getBackStackEntry(route)
        true
    } catch (_: IllegalArgumentException) {
        false
    }
}