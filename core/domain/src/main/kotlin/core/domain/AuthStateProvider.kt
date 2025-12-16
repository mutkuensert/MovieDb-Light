package core.domain

import kotlinx.coroutines.flow.StateFlow

interface AuthStateProvider {
    val loggedIn: StateFlow<Boolean>
}