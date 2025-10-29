package core.data

import core.domain.AuthState
import kotlinx.coroutines.flow.StateFlow

class AuthStateImpl(sessionManager: SessionManager) : AuthState {
    override val loggedIn: StateFlow<Boolean> = sessionManager.loggedIn
}