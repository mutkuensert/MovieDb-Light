package core.ui

import androidx.lifecycle.ViewModel
import core.domain.Failure
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

private const val KEY = "failure-channel"

private val ViewModel.failureChannel: FailureChannel
    get() {
        var closeable: FailureChannel? = getCloseable(KEY)
        if (closeable == null) {
            val failureChannel = FailureChannel(Channel(Channel.UNLIMITED))
            closeable = failureChannel
            addCloseable(KEY, failureChannel)
        }
        return closeable
    }

val ViewModel.failure get() = failureChannel.channel.receiveAsFlow()

fun ViewModel.setFailure(failure: Failure) {
    failureChannel.channel.trySend(failure)
}

class FailureChannel(val channel: Channel<Failure>) : AutoCloseable {
    override fun close() {
        channel.cancel()
    }
}
