package core.domain

import kotlinx.coroutines.flow.StateFlow

interface AuthState {
    val loggedIn: StateFlow<Boolean>
}