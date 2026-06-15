package core.data.auth

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogoutTrigger @Inject constructor() {
    private val _trigger = MutableSharedFlow<LogoutTriggerEvent>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )
    val trigger = _trigger.asSharedFlow()

    fun triggerLogout() {
        _trigger.tryEmit(LogoutTriggerEvent.LOGOUT)
    }

    fun cancelLogout() {
        _trigger.tryEmit(LogoutTriggerEvent.CANCEL)
    }
}

enum class LogoutTriggerEvent {
    LOGOUT, CANCEL
}