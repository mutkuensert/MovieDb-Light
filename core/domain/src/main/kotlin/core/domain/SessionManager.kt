package core.domain

import kotlinx.coroutines.flow.StateFlow

interface SessionManager {
    val loggedIn: StateFlow<Boolean>
}