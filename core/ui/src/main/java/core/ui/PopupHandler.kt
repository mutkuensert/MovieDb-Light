package core.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PopupHandler {
    private val _popup = MutableStateFlow<Popup?>(null)
    val popup = _popup.asStateFlow()


    fun show(popup: Popup.Builder.() -> Unit) {
        _popup.value = Popup.Builder().apply(popup).build()
    }

    fun showSimpleMessage(message: String) {
        val builder = Popup.Builder()
        builder.message = message
        _popup.value = builder.build()
    }

    fun close() {
        _popup.value = null
    }
}

data class Popup(
    val title: String?,
    val message: String,
    val showConfirmButton: Boolean,
    val confirmButtonText: String,
    val onConfirm: () -> Unit,
    val showDismissButton: Boolean,
    val dismissButtonText: String,
    val onDismiss: () -> Unit,
    val allowToDismiss: Boolean,
) {
    class Builder {
        var title: String? = null
        var message: String = ""
        var showConfirmButton: Boolean = true
        var confirmButtonText: String = ""
        var onConfirm: () -> Unit = {}
        var showDismissButton: Boolean = false
        var dismissButtonText: String = ""
        var onDismiss: () -> Unit = {}
        var allowToDismiss: Boolean = true

        fun build(): Popup {
            return Popup(
                title,
                message,
                showConfirmButton,
                confirmButtonText,
                onConfirm,
                showDismissButton,
                dismissButtonText,
                onDismiss,
                allowToDismiss,
            )
        }
    }
}
