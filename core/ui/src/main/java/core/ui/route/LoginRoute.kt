package core.ui.route

import kotlinx.serialization.Serializable

@Serializable
data class LoginRoute(val cameFromTmdbLogin: Boolean = false)