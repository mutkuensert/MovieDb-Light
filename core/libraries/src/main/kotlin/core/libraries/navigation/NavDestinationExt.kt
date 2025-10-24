package core.libraries.navigation

import androidx.navigation.NavDestination
import kotlin.reflect.KClass

fun NavDestination.isRoute(route: KClass<*>): Boolean {
    return this.route?.contains(route.java.simpleName) == true
}