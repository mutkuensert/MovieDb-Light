package moviedblight

import core.domain.AuthState
import core.domain.FetchUserRelatedDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthEventHandler(
    private val scope: CoroutineScope,
    private val authState: AuthState,
    private val fetchUserRelatedDataUseCase: FetchUserRelatedDataUseCase,
) {
    init {
        handleLoggedInState()
    }

    fun handleLoggedInState() {
        scope.launch {
            authState.loggedIn.collectLatest { loggedIn ->
                if (loggedIn) {
                    fetchUserRelatedDataUseCase.execute()
                }
            }
        }
    }
}