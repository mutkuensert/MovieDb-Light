package core.domain.auth

import kotlinx.coroutines.flow.StateFlow

interface AuthStateProvider {
    val loggedIn: StateFlow<Boolean>
}