package core.ui.route

import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailRoute(val id: Int)

@Serializable
data object SettingsRoute

@Serializable
object MoviesRoute

@Serializable
data class LoginRoute(val cameFromTmdbLogin: Boolean = false)